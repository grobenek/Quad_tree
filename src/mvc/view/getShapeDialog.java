package mvc.view;

import mvc.controller.IController;

import java.awt.event.*;
import javax.swing.*;

public class getShapeDialog extends JDialog {
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
  private IController controller;

  public getShapeDialog(JFrame mainWindow) {
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
    setTitle("Set shape of searched object");
    setLocationRelativeTo(mainWindow);
    setVisible(true);
  }

  private void onOK() {
    // add your code here
    dispose();
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }
}
