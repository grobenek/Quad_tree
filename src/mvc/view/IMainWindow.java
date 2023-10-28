package mvc.view;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import mvc.controller.IController;
import quadtree.QuadTree;

import javax.swing.*;

public interface IMainWindow {
  void setParcelTreeInfo(QuadTree<Parcel> parcelQuadTree);
  void setPropertyTreeInfo(QuadTree<Property> propertyQuadTree);
  void setController(IController controller);
  void searchProperties(Rectangle area);
  void searchParcels(Rectangle area);
  void searchAllObjects(Rectangle area);
  JFrame getJFrameObject();
}
