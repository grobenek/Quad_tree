package mvc.model;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import java.util.LinkedList;
import java.util.List;
import mvc.view.observable.IObserver;
import mvc.view.observable.IQuadTreeObservable;
import quadtree.IShapeData;
import quadtree.QuadTree;

public class ModelWrapper implements IModel, IQuadTreeObservable {
  QuadTree<Property> propertyQuadTree;
  QuadTree<Parcel> parcelQuadTree;

  List<IObserver> observers;

  public ModelWrapper() {
    this.observers = new LinkedList<>();
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
  public Property addProperty(int registerNumber, String description, Rectangle shape) {
    Property property = new Property(registerNumber, description, shape);

    propertyQuadTree.insert(property);

    property.setParcels(parcelQuadTree.search(shape));

    sendNotifications();

    return property;
  }

  @Override
  public void generateDataForBothTrees() {}

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
