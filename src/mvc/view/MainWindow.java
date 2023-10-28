package mvc.view;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import mvc.controller.IController;
import mvc.view.constant.DataType;
import mvc.view.constant.SearchCriteria;
import mvc.view.observable.IObservable;
import mvc.view.observable.IObserver;
import mvc.view.observable.IQuadTreeParametersObservable;
import quadtree.IShapeData;
import quadtree.QuadTree;

public class MainWindow extends JFrame implements IMainWindow, IObserver {

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
  private JButton insertNewParcelButton;
  private JButton resetTreesButton;
  private JButton editPropertyButton;
  private JButton editParcelButton;
  private JButton deletePropertyButton;
  private JButton deleteParcelButton;

  public MainWindow(IController controller) throws HeadlessException {
    this.controller = controller;

    setContentPane(mainPanel);
    setTitle("Szathmáry_AUS2 - Semestrálna práca č. 1");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(1200, 900);
    setLocationRelativeTo(null);
    setVisible(true);

    insertNewPropertyButton.addActionListener(
        e -> {
          NewPropertyDialog newPropertyDialog = new NewPropertyDialog(this, this.controller);
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
    resetTreesButton.addActionListener(e -> {
      initializeBothQuadTrees();
    });
  }

  @Override
  public void initializeBothQuadTrees() {
    InitializeQuadTree initializeParcelQuadTree = new InitializeQuadTree(this, DataType.PARCEL);
    initializeParcelQuadTree.attach(this);
    initializeParcelQuadTree.setVisible(true);

    InitializeQuadTree initializePropertyQuadTree = new InitializeQuadTree(this, DataType.PROPERTY);
    initializePropertyQuadTree.attach(this);
    initializePropertyQuadTree.setVisible(true);
  }

  @Override
  public void showPopupMessage(String message) {
    // setting maximum size - errors are often long
    JTextArea errorTextArea = new JTextArea(message);
    errorTextArea.setLineWrap(true);
    errorTextArea.setWrapStyleWord(true);
    errorTextArea.setEditable(false);

    JScrollPane scrollPane = new JScrollPane(errorTextArea);
    scrollPane.setPreferredSize(new Dimension(500, 300));

    JOptionPane.showMessageDialog(this, scrollPane, "Nastala chyba :(", JOptionPane.ERROR_MESSAGE);
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

  public void initializePropertyQuadTree(int height, Rectangle shape) {
    controller.initializePropertyQuadTree(height, shape);
  }

  public void initializeParcelQuadTree(int height, Rectangle shape) {
    controller.initializeParcelQuadTree(height, shape);
  }

  @Override
  public JFrame getJFrameObject() {
    return this;
  }

  @Override
  public void update(IObservable observable) {
    if (!(observable instanceof IQuadTreeParametersObservable)) {
      return;
    }

    if (((IQuadTreeParametersObservable) observable).getQuadTreeDataType() == DataType.PROPERTY) {
      int propertyQuadTreeHeight = ((IQuadTreeParametersObservable) observable).getQuadTreeHeight();
      Rectangle propertyQuadTreeRectangle =
          ((IQuadTreeParametersObservable) observable).getQuadTreeArea();
      initializePropertyQuadTree(propertyQuadTreeHeight, propertyQuadTreeRectangle);
    }

    if (((IQuadTreeParametersObservable) observable).getQuadTreeDataType() == DataType.PARCEL) {
      int parcelQuadTreeHeight = ((IQuadTreeParametersObservable) observable).getQuadTreeHeight();
      Rectangle parcelQuadTreeRectangle =
          ((IQuadTreeParametersObservable) observable).getQuadTreeArea();
      initializeParcelQuadTree(parcelQuadTreeHeight, parcelQuadTreeRectangle);
    }
  }
}
