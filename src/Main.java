import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import mvc.controller.Controller;
import mvc.model.ModelWrapper;
import mvc.view.MainWindow;

public class Main {
  public static void main(String[] args) {
    ModelWrapper model = new ModelWrapper();
    MainWindow mainWindow = new MainWindow();
    Controller controller = new Controller(model, mainWindow);

    Rectangle rectangle =
        new Rectangle(
            new GpsCoordinates(Direction.S, 0.0, Direction.E, 0.0),
            new GpsCoordinates(Direction.S, 10.0, Direction.E, 10.0));

    model.intializeParcelQuadTree(2, rectangle);
    model.intializePropertyQuadTree(10, rectangle);
  }
}
