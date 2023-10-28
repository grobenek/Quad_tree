package mvc.controller;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import java.util.List;
import mvc.view.observable.IObserver;
import quadtree.IShapeData;

public interface IController extends IObserver {
  List<Parcel> searchParcelsInGivenShape(Rectangle shapeToSearchIn);
  List<Property> searchPropertiesInGivenShape(Rectangle shapeToSearchIn);
  List<? extends IShapeData>[] searchAllObjectsInGivenShape(Rectangle shapeToSearchIn);
  Property addProperty(int registerNumber, String description, Rectangle shape);
  void initializePropertyQuadTree(int height, Rectangle shape);
  void initializeParcelQuadTree(int height, Rectangle shape);
}
