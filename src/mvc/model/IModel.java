package mvc.model;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import mvc.view.observable.IObservable;
import quadtree.IShapeData;

import java.util.List;

public interface IModel extends IObservable {
    List<Parcel> searchParcelsInGivenShape(Rectangle shapeToSearchIn);
    List<Property> searchPropertiesInGivenShape(Rectangle shapeToSearchIn);
    List<? extends IShapeData>[] searchAllObjectsInGivenShape(Rectangle shapeToSearchIn);
    void addProperty(int registerNumber, String description, Rectangle shape);
    void addParcel(int parcelNumber, String description, Rectangle shape);
    void generateDataForBothTrees();
    void initializePropertyQuadTree(int height, Rectangle shape);
    void initializeParcelQuadTree(int height, Rectangle shape);
}
