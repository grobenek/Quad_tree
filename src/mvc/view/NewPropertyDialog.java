package mvc.view;

import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import java.awt.event.*;
import javax.swing.*;
import mvc.controller.IController;

public class NewPropertyDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField registrationNumberTextField;
  private JLabel registrationNumberLabel;
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
  private IController controller;

  public NewPropertyDialog(JFrame mainWindow, IController controller) {
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

    controller.addProperty(
        Integer.parseInt(registrationNumberTextField.getText()),
        descriptionTextField.getText(),
        new Rectangle(bottomLeftPoint, topRightPoint));
    dispose();
  }

  private void onCancel() {
    dispose();
  }
}
