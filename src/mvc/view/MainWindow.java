package mvc.view;

import entity.Parcel;
import entity.Property;
import quadtree.IShapeData;
import quadtree.QuadTree;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
  private JButton insertNewPropertyButton;
  private JButton seachPropertiesButton;
  private JButton searchParcelsButton;
  private JButton searchAllButton;
  private JTextPane resultText;
  private JTextPane parcelTreeInfo;
  private JTextPane propertyTreeInfo;
  private JPanel mainPanel;

  public MainWindow() throws HeadlessException {
    setContentPane(mainPanel);
    setTitle("Szathmáry_AUS2 - Semestrálna práca č. 1");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(600, 500);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public void setParcelTreeInfo(QuadTree<Parcel> parcelQuadTree) {
    setTextInfoAboutQuadTree(parcelQuadTree, parcelTreeInfo);
  }

  public void setPropertyTreeInfo(QuadTree<Property> propertyQuadTree) {
    setTextInfoAboutQuadTree(propertyQuadTree, propertyTreeInfo);
  }

  private void setTextInfoAboutQuadTree(QuadTree<? extends IShapeData> propertyQuadTree, JTextPane propertyTreeInfo) {
    propertyTreeInfo.setText(
        String.format(
            "Max height: %d\nShape: {x1: %f, y1: %f, x2: %f, y2: %f}\nWidth: %f\nLength: %f\nSize: %d\nItems in root: %d\nItems in North_West: %d\nItems in North_East: %d\nItems in South_West: %d\nItems in South_East: %d\nHealth: %f",
            propertyQuadTree.getHeight(),
            propertyQuadTree.getShape().getFirstPoint().widthCoordinate(),
            propertyQuadTree.getShape().getFirstPoint().lengthCoordinate(),
            propertyQuadTree.getShape().getSecondPoint().widthCoordinate(),
            propertyQuadTree.getShape().getSecondPoint().lengthCoordinate(),
            propertyQuadTree.getShape().getWidth(),
            propertyQuadTree.getShape().getLength(),
            propertyQuadTree.getSize(),
            propertyQuadTree.getItemsInRoot(),
            propertyQuadTree.getItemsInNorthWest(),
            propertyQuadTree.getItemsInNorthEast(),
            propertyQuadTree.getItemsInSouthWest(),
            propertyQuadTree.getItemsInSouthEast(),
            propertyQuadTree.getHealthOfQuadTree()));
  }
}
