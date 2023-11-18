package mvc.model;

import entity.Parcel;
import entity.Property;
import entity.SpatialData;
import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import mvc.view.constant.DataType;
import mvc.view.observable.IObserver;
import mvc.view.observable.IQuadTreeObservable;
import quadtree.IShapeData;
import quadtree.QuadTree;
import util.file.IFileBuilder;
import util.file.IOManager;

public class ModelWrapper implements IModel, IQuadTreeObservable {
  QuadTree<Property> propertyQuadTree;
  QuadTree<Parcel> parcelQuadTree;
  List<IObserver> observers;
  Random randomGenerator;

  public ModelWrapper() {
    this.observers = new LinkedList<>();
    this.randomGenerator = new Random();
  }

  @Override
  public void initializePropertyQuadTree(int height, Rectangle shape) {
    propertyQuadTree = new QuadTree<>(height, shape);
    sendNotifications();
  }

  @Override
  public void initializeParcelQuadTree(int height, Rectangle shape) {
    parcelQuadTree = new QuadTree<>(height, shape);
    sendNotifications();
  }

  @Override
  public void generateData(int numberOfProperties, int numberOfParcels) {
    generateData(numberOfProperties, DataType.PROPERTY);
    generateData(numberOfParcels, DataType.PARCEL);
    sendNotifications();
  }

  @Override
  public void saveDataFromFile(String pathToFile, DataType datatype, IFileBuilder fileBuilder)
      throws IOException {
    List<? extends SpatialData<?>> dataToSave;

    switch (datatype) {
      case PARCEL -> dataToSave = parcelQuadTree.search(parcelQuadTree.getShape());
      case PROPERTY -> dataToSave = propertyQuadTree.search(propertyQuadTree.getShape());
      default -> throw new IllegalArgumentException(
          String.format("Unsupported datatype %s", datatype.name()));
    }

    new IOManager(fileBuilder).saveToFile(pathToFile, dataToSave);
  }

  @Override
  public void loadDataFromFile(String pathToFile, IFileBuilder fileBuilder) throws IOException {
    IOManager ioManager = new IOManager(fileBuilder);
    ioManager.loadFromFile(pathToFile);

    List<SpatialData<?>> loadedItems = fileBuilder.getLoadedData();

    if (loadedItems.isEmpty()) {
      return;
    }

    if (loadedItems.get(0) instanceof Property) {
      propertyQuadTree = new QuadTree<>(propertyQuadTree.getHeight(), propertyQuadTree.getShape());
      for (SpatialData<?> loadedItem : loadedItems) {
        Property loadedProperty = (Property) loadedItem;
        addProperty(
            loadedProperty.getIdentificationNumber(),
            loadedProperty.getDescription(),
            loadedProperty.getShapeOfData(),
            true);
      }
    } else if (loadedItems.get(0) instanceof Parcel) {
      parcelQuadTree = new QuadTree<>(parcelQuadTree.getHeight(), parcelQuadTree.getShape());
      for (SpatialData<?> loadedItem : loadedItems) {
        Parcel loadedParcel = (Parcel) loadedItem;
        addParcel(
            loadedParcel.getIdentificationNumber(),
            loadedParcel.getDescription(),
            loadedParcel.getShapeOfData(),
            true);
      }
    } else {
      throw new IllegalStateException(
          String.format(
              "Unsupported object %s found when loading file %s",
              loadedItems.get(0).getClass().getName(), pathToFile));
    }
  }

  @Override
  public void optimizeTrees() {
    propertyQuadTree.optimize();
    parcelQuadTree.optimize();
    sendNotifications();
  }

  private void generateData(int numberOfItemsToInsert, DataType dataTypeToInsert) {
    for (int i = 0; i < numberOfItemsToInsert; i++) {
      Rectangle quadTreeShape;

      switch (dataTypeToInsert) {
        case PROPERTY -> quadTreeShape = propertyQuadTree.getShape();
        case PARCEL -> quadTreeShape = parcelQuadTree.getShape();
        default -> throw new IllegalArgumentException(
            "Invalid data type: " + dataTypeToInsert.name());
      }

      double x1 =
          (randomGenerator.nextDouble(
              quadTreeShape.getFirstPoint().widthCoordinate(),
              quadTreeShape.getSecondPoint().widthCoordinate()));
      double y1 =
          (randomGenerator.nextDouble(
              quadTreeShape.getFirstPoint().lengthCoordinate(),
              quadTreeShape.getSecondPoint().lengthCoordinate()));
      double x2 =
          (randomGenerator.nextDouble(
              quadTreeShape.getFirstPoint().widthCoordinate(),
              quadTreeShape.getSecondPoint().widthCoordinate()));
      double y2 =
          (randomGenerator.nextDouble(
              quadTreeShape.getFirstPoint().lengthCoordinate(),
              quadTreeShape.getSecondPoint().lengthCoordinate()));

      GpsCoordinates firstPointOfItem = new GpsCoordinates(Direction.S, x1, Direction.W, y1);
      GpsCoordinates secondPointOfItem = new GpsCoordinates(Direction.S, x2, Direction.W, y2);
      switch (dataTypeToInsert) {
        case PROPERTY -> addProperty(
            i, "Popis " + i, new Rectangle(firstPointOfItem, secondPointOfItem), false);
        case PARCEL -> addParcel(
            i, "Popis " + i, new Rectangle(firstPointOfItem, secondPointOfItem), false);
        default -> throw new IllegalArgumentException(
            "Invalid data type: " + dataTypeToInsert.name());
      }
    }
  }

  @Override
  public List<Parcel> searchParcelsInGivenShape(Rectangle shapeToSearchIn) {
    return parcelQuadTree.search(shapeToSearchIn);
  }

  @Override
  public List<Property> searchPropertiesInGivenShape(Rectangle shapeToSearchIn) {
    return propertyQuadTree.search(shapeToSearchIn);
  }

  /**
   * Search for all Parcels and Properties in given shape
   *
   * @param shapeToSearchIn shape to seach items in
   * @return Return array of two result lists: <br>
   *     * First list is containing Parcels <br>
   *     * Second list is containing Properties
   */
  @Override
  public List<? extends IShapeData>[] searchAllObjectsInGivenShape(Rectangle shapeToSearchIn) {
    List<Property> propertiesResult = propertyQuadTree.search(shapeToSearchIn);
    List<Parcel> parcelsResult = parcelQuadTree.search(shapeToSearchIn);

    return new List[] {parcelsResult, propertiesResult};
  }

  @Override
  public void addProperty(
      int registerNumber, String description, Rectangle shape, boolean shouldNotifyAboutChanges) {
    Property property = new Property(registerNumber, description, shape);

    propertyQuadTree.insert(property);

    List<Parcel> parcels = parcelQuadTree.search(shape);

    for (Parcel parcel : parcels) {
      property.addRelatedData(parcel);
      parcel.addRelatedData(property);
    }

    if (!shouldNotifyAboutChanges) {
      return;
    }

    sendNotifications();
  }

  @Override
  public void addParcel(
      int parcelNumber, String description, Rectangle shape, boolean shouldNotifyAboutChanges) {
    Parcel parcel = new Parcel(parcelNumber, description, shape);

    parcelQuadTree.insert(parcel);

    List<Property> properties = propertyQuadTree.search(shape);

    for (Property property : properties) {
      parcel.addRelatedData(property);
      property.addRelatedData(parcel);
    }

    if (!shouldNotifyAboutChanges) {
      return;
    }

    sendNotifications();
  }

  @Override
  public void deleteProperty(Property propertyToDelete) {
    List<Parcel> parcels = parcelQuadTree.search(propertyToDelete.getShapeOfData());
    for (Parcel parcel : parcels) {
      parcel.removeRelatedData(propertyToDelete);
    }

    propertyQuadTree.deleteData(propertyToDelete);
    sendNotifications();
  }

  @Override
  public void deleteParcel(Parcel parcelToDelete) {
    List<Property> properties = propertyQuadTree.search(parcelToDelete.getShapeOfData());
    for (Property property : properties) {
      property.removeRelatedData(parcelToDelete);
    }

    parcelQuadTree.deleteData(parcelToDelete);
    sendNotifications();
  }

  @Override
  public void attach(IObserver observer) {
    observers.add(observer);
  }

  @Override
  public void detach(IObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void sendNotifications() {
    for (IObserver observer : observers) {
      observer.update(this);
    }
  }

  @Override
  public QuadTree<? extends IShapeData>[] getTrees() {
    if (parcelQuadTree == null && propertyQuadTree == null) {
      return new QuadTree[0];
    } else if (parcelQuadTree == null) {
      return new QuadTree[] {propertyQuadTree};
    } else if (propertyQuadTree == null) {
      return new QuadTree[] {parcelQuadTree};
    } else {
      return new QuadTree[] {parcelQuadTree, propertyQuadTree};
    }
  }
}
