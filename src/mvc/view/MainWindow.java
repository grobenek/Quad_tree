package mvc.view;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import mvc.controller.IController;
import quadtree.IShapeData;
import quadtree.QuadTree;

public class MainWindow extends JFrame implements IMainWindow {

  private IController controller;
  private JButton insertNewPropertyButton;
  private JButton seachPropertiesButton;
  private JButton searchParcelsButton;
  private JButton searchAllButton;
  private JTextPane resultText;
  private JTextPane parcelTreeInfo;
  private JTextPane propertyTreeInfo;
  private JPanel mainPanel;
  private JButton generateDataButton;

  public MainWindow() throws HeadlessException {
    setContentPane(mainPanel);
    setTitle("Szathmáry_AUS2 - Semestrálna práca č. 1");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(1200, 900);
    setLocationRelativeTo(null);
    setVisible(true);

    insertNewPropertyButton.addActionListener(
        e -> {
          NewPropertyDialog newPropertyDialog = new NewPropertyDialog(this, controller);
        });

    seachPropertiesButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog = new GetShapeDialog(this, SearchCriteria.PROPERTIES);
        });

    searchParcelsButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog = new GetShapeDialog(this, SearchCriteria.PARCELS);
        });

    searchAllButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog = new GetShapeDialog(this, SearchCriteria.ALL);
        });
  }

  public void setParcelTreeInfo(QuadTree<Parcel> parcelQuadTree) {
    setTextInfoAboutQuadTree(parcelQuadTree, parcelTreeInfo);
  }

  public void setPropertyTreeInfo(QuadTree<Property> propertyQuadTree) {
    setTextInfoAboutQuadTree(propertyQuadTree, propertyTreeInfo);
  }

  private void setTextInfoAboutQuadTree(
      QuadTree<? extends IShapeData> propertyQuadTree, JTextPane propertyTreeInfo) {
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

  public void setController(IController controller) {
    this.controller = controller;
  }

  @Override
  public void searchProperties(Rectangle area) {
    List<Property> result = controller.searchPropertiesInGivenShape(area);
    resultText.setText(result.toString());
  }

  @Override
  public void searchParcels(Rectangle area) {
    throw new IllegalStateException("Not implemented yet!");
  }

  @Override
  public void searchAllObjects(Rectangle area) {
    List<? extends IShapeData>[] result = controller.searchAllObjectsInGivenShape(area);
    resultText.setText(Arrays.toString(result));
  }

  @Override
  public JFrame getJFrameObject() {
    return this;
  }
}
