package mvc.controller;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import mvc.view.IObserver;
import quadtree.IShapeData;

import java.util.List;

public interface IController extends IObserver {
    List<Parcel> searchParcelsInGivenShape(Rectangle shapeToSearchIn);
    List<Property> searchPropertiesInGivenShape(Rectangle shapeToSearchIn);
    List<? extends IShapeData>[] searchAllObjectsInGivenShape(Rectangle shapeToSearchIn);
    Property addProperty(int registerNumber, String description, Rectangle shape);
}
