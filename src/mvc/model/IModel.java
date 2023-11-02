package mvc.model;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import java.io.IOException;
import java.util.List;
import mvc.view.constant.DataType;
import mvc.view.observable.IObservable;
import quadtree.IShapeData;
import util.file.IFileBuilder;

public interface IModel extends IObservable {
  List<Parcel> searchParcelsInGivenShape(Rectangle shapeToSearchIn);

  List<Property> searchPropertiesInGivenShape(Rectangle shapeToSearchIn);

  List<? extends IShapeData>[] searchAllObjectsInGivenShape(Rectangle shapeToSearchIn);

  void addProperty(int registerNumber, String description, Rectangle shape);

  void addParcel(int parcelNumber, String description, Rectangle shape);

  void deleteProperty(Property propertyToDelete);

  void deleteParcel(Parcel parcelToDelete);

  void initializePropertyQuadTree(int height, Rectangle shape);

  void initializeParcelQuadTree(int height, Rectangle shape);

  void generateData(int numberOfProperties, int numberOfParcels);

  void saveDataFromFile(String pathToFile, DataType dataType, IFileBuilder fileBuilder)
      throws IOException;

  void loadDataFromFile(String pathToFile, IFileBuilder fileBuilder) throws IOException;
}
