import mvc.controller.Controller;
import mvc.model.ModelWrapper;
import mvc.view.MainWindow;

public class Main {
  public static void main(String[] args) {
    ModelWrapper model = new ModelWrapper();
    Controller controller = new Controller(model);
    MainWindow mainWindow = new MainWindow(controller);
    controller.setView(mainWindow);
  }
}
