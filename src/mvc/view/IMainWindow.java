package mvc.view;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import quadtree.QuadTree;

import javax.swing.*;

public interface IMainWindow {
  void setParcelTreeInfo(QuadTree<Parcel> parcelQuadTree);
  void setPropertyTreeInfo(QuadTree<Property> propertyQuadTree);
  void searchProperties(Rectangle area);
  void searchParcels(Rectangle area);
  void searchAllObjects(Rectangle area);
  void editProperty(Rectangle area);
  void editParcel(Rectangle area);
  void deleteProperty(Rectangle shape);
  void deleteParcel(Rectangle shape);
  void initializeBothQuadTrees();
  void showPopupMessage(String message);
  JFrame getJFrameObject();
}
