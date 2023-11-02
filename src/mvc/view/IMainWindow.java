package mvc.view;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import java.io.IOException;
import javax.swing.*;
import mvc.view.constant.DataType;
import quadtree.QuadTree;
import util.file.IFileBuilder;

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

  void generateData(int numberOfProperties, int numberOfParcels);

  void saveDataFromFile(String pathToFile, DataType dataType, IFileBuilder fileBuilder)
      throws IOException;

  void loadDataFromFile(String pathToFile, IFileBuilder fileBuilder);

  void showPopupMessage(String message);

  JFrame getJFrameObject();
}
