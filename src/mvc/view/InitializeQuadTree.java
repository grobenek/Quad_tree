package mvc.view;

import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import mvc.view.constant.DataType;
import mvc.view.observable.IObserver;
import mvc.view.observable.IQuadTreeParametersObservable;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;

public class InitializeQuadTree extends JDialog implements IQuadTreeParametersObservable {
  private List<IObserver> observers;
  private DataType quadTreeDataType;
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JPanel propertyJPanel;
  private JTextField x1TextField;
  private JLabel x1Label;
  private JTextField y1TextField;
  private JTextField x2TextField;
  private JTextField y2TextField;
  private JTextField maxHeightTextField;
  private JLabel maxHeightLabel;
  private JLabel y1Label;
  private JLabel y2Label;
  private JLabel x2Label;

  public InitializeQuadTree(IMainWindow mainWindow, DataType quadTreeDataType) {
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    observers = new LinkedList<>();
    this.quadTreeDataType = quadTreeDataType;

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

    setSize(500, 400);
    setAutoRequestFocus(true);
    setTitle(String.format("Initialize %s quad tree", quadTreeDataType.name()));
    setLocationRelativeTo(mainWindow.getJFrameObject());
  }

  private void onOK() {
    sendNotifications();
    dispose();
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }

  @Override
  public void attach(IObserver observer) {
    observers.add(observer);
  }

  @Override
  public void detach(IObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void sendNotifications() {
    for (IObserver observer : observers) {
      observer.update(this);
    }
  }

  @Override
  public int getQuadTreeHeight() {
    return Integer.parseInt(maxHeightTextField.getText());
  }

  @Override
  public Rectangle getQuadTreeArea() {
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

    return new Rectangle(bottomLeftPoint, topRightPoint);
  }

  @Override
  public DataType getQuadTreeDataType() {
    return quadTreeDataType;
  }
}
