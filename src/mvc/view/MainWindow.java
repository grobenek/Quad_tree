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
import mvc.view.constant.OperationType;
import mvc.view.constant.SearchCriteria;
import mvc.view.observable.IObservable;
import mvc.view.observable.IObserver;
import mvc.view.observable.IQuadTreeParametersObservable;
import quadtree.IShapeData;
import quadtree.QuadTree;

public class MainWindow extends JFrame implements IMainWindow, IObserver {

  private final IController controller;
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
          NewDataDialog newPropertyDialog =
              new NewDataDialog(this, this.controller, DataType.PROPERTY);
        });

    insertNewParcelButton.addActionListener(
        e -> {
          NewDataDialog newParcelDialog = new NewDataDialog(this, this.controller, DataType.PARCEL);
        });

    seachPropertiesButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog =
              new GetShapeDialog(this, SearchCriteria.PROPERTIES, OperationType.SEARCH);
        });

    searchParcelsButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog =
              new GetShapeDialog(this, SearchCriteria.PARCELS, OperationType.SEARCH);
        });

    searchAllButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog =
              new GetShapeDialog(this, SearchCriteria.ALL, OperationType.SEARCH);
        });

    resetTreesButton.addActionListener(
        e -> {
          initializeBothQuadTrees();
          resultText.setText("");
        });

    editPropertyButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog =
              new GetShapeDialog(this, SearchCriteria.PROPERTIES, OperationType.EDIT);
        });

    editParcelButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog =
              new GetShapeDialog(this, SearchCriteria.PARCELS, OperationType.EDIT);
        });

    deletePropertyButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog =
              new GetShapeDialog(this, SearchCriteria.PROPERTIES, OperationType.DELETE);
        });

    deleteParcelButton.addActionListener(
        e -> {
          GetShapeDialog getShapeDialog =
              new GetShapeDialog(this, SearchCriteria.PARCELS, OperationType.DELETE);
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
      QuadTree<? extends IShapeData> quadTree, JTextPane QuadTreeInfoTextPane) {
    QuadTreeInfoTextPane.setText(
        String.format(
            "Max height: %d\nShape: {x1: %f, y1: %f, x2: %f, y2: %f}\nWidth: %f\nLength: %f\nSize: %d\nItems in root: %d\nItems in North_West: %d\nItems in North_East: %d\nItems in South_West: %d\nItems in South_East: %d\nHealth: %f",
            quadTree.getHeight(),
            quadTree.getShape().getFirstPoint().widthCoordinate(),
            quadTree.getShape().getFirstPoint().lengthCoordinate(),
            quadTree.getShape().getSecondPoint().widthCoordinate(),
            quadTree.getShape().getSecondPoint().lengthCoordinate(),
            quadTree.getShape().getWidth(),
            quadTree.getShape().getLength(),
            quadTree.getSize(),
            quadTree.getItemsInRoot(),
            quadTree.getItemsInNorthWest(),
            quadTree.getItemsInNorthEast(),
            quadTree.getItemsInSouthWest(),
            quadTree.getItemsInSouthEast(),
            quadTree.getHealthOfQuadTree()));
  }

  @Override
  public void searchProperties(Rectangle area) {
    List<Property> result = controller.searchPropertiesInGivenShape(area);
    resultText.setText(result.toString());
  }

  @Override
  public void searchParcels(Rectangle area) {
    List<Parcel> result = controller.searchParcelsInGivenShape(area);
    resultText.setText(result.toString());
  }

  @Override
  public void searchAllObjects(Rectangle area) {
    List<? extends IShapeData>[] result = controller.searchAllObjectsInGivenShape(area);
    resultText.setText(Arrays.toString(result));
  }

  @Override
  public void editProperty(Rectangle area) {
    List<Property> result = controller.searchPropertiesInGivenShape(area);

    JComboBox<Property> propertyComboBox = new JComboBox<>(result.toArray(new Property[0]));

    JPanel panel = new JPanel();
    panel.add(propertyComboBox);
    int chosenOption =
        JOptionPane.showConfirmDialog(this, panel, "Select Property", JOptionPane.OK_CANCEL_OPTION);

    if (chosenOption == JOptionPane.OK_OPTION) {
      Property chosenProperty = (Property) propertyComboBox.getSelectedItem();

      NewDataDialog newDataDialog =
          new NewDataDialog(this, controller, DataType.PROPERTY, chosenProperty);
    }
  }

  @Override
  public void editParcel(Rectangle area) {
    List<Parcel> result = controller.searchParcelsInGivenShape(area);

    JComboBox<Parcel> parcelJComboBox = new JComboBox<>(result.toArray(new Parcel[0]));

    JPanel panel = new JPanel();
    panel.add(parcelJComboBox);
    int chosenOption =
        JOptionPane.showConfirmDialog(this, panel, "Select Parcel", JOptionPane.OK_CANCEL_OPTION);

    if (chosenOption == JOptionPane.OK_OPTION) {
      Parcel chosenParcel = (Parcel) parcelJComboBox.getSelectedItem();

      NewDataDialog newDataDialog =
          new NewDataDialog(this, controller, DataType.PARCEL, chosenParcel);
    }
  }

  @Override
  public void deleteProperty(Rectangle area) {
    List<Property> result = controller.searchPropertiesInGivenShape(area);

    JComboBox<Property> propertyJComboBox = new JComboBox<>(result.toArray(new Property[0]));

    JPanel panel = new JPanel();
    panel.add(propertyJComboBox);
    int chosenOption =
        JOptionPane.showConfirmDialog(this, panel, "Select Property", JOptionPane.OK_CANCEL_OPTION);

    if (chosenOption == JOptionPane.OK_OPTION) {
      Property chosenProperty = (Property) propertyJComboBox.getSelectedItem();

      controller.deleteProperty(chosenProperty);
    }
  }

  @Override
  public void deleteParcel(Rectangle area) {
    List<Parcel> result = controller.searchParcelsInGivenShape(area);

    JComboBox<Parcel> parcelJComboBox = new JComboBox<>(result.toArray(new Parcel[0]));

    JPanel panel = new JPanel();
    panel.add(parcelJComboBox);
    int chosenOption =
        JOptionPane.showConfirmDialog(this, panel, "Select Parcel", JOptionPane.OK_CANCEL_OPTION);

    if (chosenOption == JOptionPane.OK_OPTION) {
      Parcel chosenParcel = (Parcel) parcelJComboBox.getSelectedItem();

      controller.deleteParcel(chosenParcel);
    }
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
