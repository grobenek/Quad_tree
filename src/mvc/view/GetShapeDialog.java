package mvc.view;

import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import java.awt.event.*;
import javax.swing.*;

public class GetShapeDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField x1TextField;
  private JLabel x1Label;
  private JTextField y1TextField;
  private JTextField x2TextField;
  private JLabel x2Label;
  private JTextField y2TextField;
  private JLabel y2Label;
  private final IMainWindow mainWindow;
  private final SearchCriteria searchCriteria;

  public GetShapeDialog(IMainWindow mainWindow, SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
    this.mainWindow = mainWindow;
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

    setSize(200, 250);
    setAutoRequestFocus(true);
    setTitle("Set shape of searched object");
    setLocationRelativeTo(mainWindow.getJFrameObject());
    setVisible(true);
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

    switch (searchCriteria) {
      case PROPERTIES -> mainWindow.searchProperties(new Rectangle(bottomLeftPoint, topRightPoint));
      case PARCELS -> mainWindow.searchParcels(new Rectangle(bottomLeftPoint, topRightPoint));
      case ALL -> mainWindow.searchAllObjects(new Rectangle(bottomLeftPoint, topRightPoint));
    }

    dispose();
  }

  private void onCancel() {
    dispose();
  }
}
