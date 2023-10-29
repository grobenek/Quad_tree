package mvc.view;

import entity.Parcel;
import entity.Property;
import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;
import mvc.controller.IController;
import mvc.view.constant.DataType;

public class NewDataDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField identificationNumberNumberTextField;
  private JLabel identificationNumberLabel;
  private JTextField descriptionTextField;
  private JLabel descriptionLabel;
  private JPanel shapeJPanel;
  private JLabel shapeLabel;
  private JTextField x1TextField;
  private JLabel x1Label;
  private JTextField y1TextField;
  private JLabel y1Label;
  private JTextField x2TextField;
  private JTextField y2TextField;
  private JLabel x2Label;
  private JLabel y2Label;
  private final IController controller;
  private final DataType dataType;

  public NewDataDialog(
      JFrame mainWindow,
      IController controller,
      DataType dataType,
      Object dataToFillIntoTextFields) {
    this.dataType = dataType;
    this.controller = controller;

    if (dataToFillIntoTextFields instanceof Parcel parcelToAddData) {
      identificationNumberNumberTextField.setText(
          String.valueOf(parcelToAddData.getParcelNumber()));
      descriptionTextField.setText(parcelToAddData.getDescription());

      Rectangle shapeOfParcel = parcelToAddData.getShapeOfData();
      x1TextField.setText(String.valueOf(shapeOfParcel.getFirstPoint().widthCoordinate()));
      y1TextField.setText(String.valueOf(shapeOfParcel.getFirstPoint().lengthCoordinate()));
      x2TextField.setText(String.valueOf(shapeOfParcel.getSecondPoint().widthCoordinate()));
      y2TextField.setText(String.valueOf(shapeOfParcel.getSecondPoint().lengthCoordinate()));
    }

    if (dataToFillIntoTextFields instanceof Property propertyToAdd) {
      identificationNumberNumberTextField.setText(
          String.valueOf(propertyToAdd.getRegisterNumber()));
      descriptionTextField.setText(propertyToAdd.getDescription());

      Rectangle shapeOfParcel = propertyToAdd.getShapeOfData();
      x1TextField.setText(String.valueOf(shapeOfParcel.getFirstPoint().widthCoordinate()));
      y1TextField.setText(String.valueOf(shapeOfParcel.getFirstPoint().lengthCoordinate()));
      x2TextField.setText(String.valueOf(shapeOfParcel.getSecondPoint().widthCoordinate()));
      y2TextField.setText(String.valueOf(shapeOfParcel.getSecondPoint().lengthCoordinate()));
    }

    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(e -> onOK(dataToFillIntoTextFields));

    buttonCancel.addActionListener(e -> onCancel());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            onCancel();
          }
        });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(
        e -> onCancel(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    setSize(200, 400);
    setAutoRequestFocus(true);
    setTitle("Edit Property");
    setLocationRelativeTo(mainWindow);
    setVisible(true);
  }

  public NewDataDialog(JFrame mainWindow, IController controller, DataType dataType) {
    this.dataType = dataType;
    this.controller = controller;
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(e -> onOK());

    buttonCancel.addActionListener(e -> onCancel());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(
        new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            onCancel();
          }
        });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(
        e -> onCancel(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    setSize(200, 400);
    setAutoRequestFocus(true);
    setTitle("Create new Property");
    setLocationRelativeTo(mainWindow);
    setVisible(true);
  }

  private void onOK(Object filledData) {

    GpsCoordinates bottomLeftPoint =
        new GpsCoordinates(
            Direction.S,
            Float.parseFloat(x1TextField.getText()),
            Direction.W,
            Float.parseFloat(y1TextField.getText()));
    GpsCoordinates topRightPoint =
        new GpsCoordinates(
            Direction.N,
            Float.parseFloat(x2TextField.getText()),
            Direction.E,
            Float.parseFloat(y2TextField.getText()));

    Rectangle rectangleOfEditedData = new Rectangle(bottomLeftPoint, topRightPoint);

    boolean hasIdentificationNumberChanged = false;
    boolean hasDescriptionChanged = false;
    boolean haveCoordinatesChanged = false;

    if (filledData instanceof Parcel filledParcel) {

      if ((Integer.parseInt(identificationNumberNumberTextField.getText()))
          != filledParcel.getParcelNumber()) {
        hasIdentificationNumberChanged = true;
      }

      if (!Objects.equals(descriptionTextField.getText(), filledParcel.getDescription())) {
        hasDescriptionChanged = true;
      }

      if (!(rectangleOfEditedData.equals((filledParcel.getShapeOfData())))) {
        haveCoordinatesChanged = true;
      }

      // only identification number or description changed - no need to reinsert data
      if (!haveCoordinatesChanged) {
        if (hasDescriptionChanged) {
          filledParcel.setDescription(descriptionTextField.getText());
        }

        if (hasIdentificationNumberChanged) {
          filledParcel.setParcelNumber(
              Integer.parseInt(identificationNumberNumberTextField.getText()));
        }
      }

      // need to reinsert edited data
      if (haveCoordinatesChanged) {
        controller.deleteParcel(filledParcel);
        controller.addParcel(
            Integer.parseInt(identificationNumberNumberTextField.getText()),
            descriptionTextField.getText(),
            rectangleOfEditedData);
      }
    }

    if (filledData instanceof Property filledProperty) {
      if ((Integer.parseInt(identificationNumberNumberTextField.getText()))
          != filledProperty.getRegisterNumber()) {
        hasIdentificationNumberChanged = true;
      }

      if (!Objects.equals(descriptionTextField.getText(), filledProperty.getDescription())) {
        hasDescriptionChanged = true;
      }

      if (!(rectangleOfEditedData.equals(filledProperty.getShapeOfData()))) {
        haveCoordinatesChanged = true;
      }

      // only identification number or description changed - no need to reinsert data
      if (!haveCoordinatesChanged) {
        if (hasDescriptionChanged) {
          filledProperty.setDescription(descriptionTextField.getText());
        }

        if (hasIdentificationNumberChanged) {
          filledProperty.setRegisterNumber(
              Integer.parseInt(identificationNumberNumberTextField.getText()));
        }
      }

      // need to reinsert edited data
      if (haveCoordinatesChanged) {
        controller.deleteProperty(filledProperty);
        controller.addProperty(
            Integer.parseInt(identificationNumberNumberTextField.getText()),
            descriptionTextField.getText(),
            rectangleOfEditedData);
      }
    }

    dispose();
  }

  private void onOK() {
    GpsCoordinates bottomLeftPoint =
        new GpsCoordinates(
            Direction.S,
            Float.parseFloat(x1TextField.getText()),
            Direction.W,
            Float.parseFloat(y1TextField.getText()));
    GpsCoordinates topRightPoint =
        new GpsCoordinates(
            Direction.N,
            Float.parseFloat(x2TextField.getText()),
            Direction.E,
            Float.parseFloat(y2TextField.getText()));

    switch (dataType) {
      case PROPERTY -> {
        controller.addProperty(
            Integer.parseInt(identificationNumberNumberTextField.getText()),
            descriptionTextField.getText(),
            new Rectangle(bottomLeftPoint, topRightPoint));
      }
      case PARCEL -> {
        controller.addParcel(
            Integer.parseInt(identificationNumberNumberTextField.getText()),
            descriptionTextField.getText(),
            new Rectangle(bottomLeftPoint, topRightPoint));
      }
    }
    dispose();
  }

  private void onCancel() {
    dispose();
  }
}
