import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import quadtree.QuadTree;
import test.IntegerShape;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
  public static void main(String[] args) {
    int maxItems = 400_000;
    int increment = 500;
    Random random = new Random();
    try {
      FileWriter csvWriter = new FileWriter("insertion_times.csv");
      csvWriter.append("Number of Items,Time (ms)\n");

      for (int numItems = 100; numItems <= maxItems; numItems += increment) {
        int maxHeight = 150; // Set your desired max height here
        Rectangle quadTreeRectangle =
            new Rectangle(
                new GpsCoordinates(Direction.S, 0, Direction.W, 0),
                new GpsCoordinates(
                    Direction.S,
                    1000,
                    Direction.W,
                    1000)); // Set your desired rectangle coordinates here
        QuadTree<IntegerShape> quadTree = new QuadTree<>(maxHeight, quadTreeRectangle);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numItems; i++) {
         GpsCoordinates firstPointOfItem =
            new GpsCoordinates(
                Direction.S,
                (random.nextDouble(
                    quadTreeRectangle.getFirstPoint().widthCoordinate(),
                    quadTreeRectangle.getSecondPoint().widthCoordinate())),
                Direction.W,
                (random.nextDouble(
                    quadTreeRectangle.getFirstPoint().lengthCoordinate(),
                    quadTreeRectangle.getSecondPoint().lengthCoordinate())));
        GpsCoordinates secondPointOfItem =
            new GpsCoordinates(
                Direction.S,
                (random.nextDouble(
                    quadTreeRectangle.getFirstPoint().widthCoordinate(),
                    quadTreeRectangle.getSecondPoint().widthCoordinate())),
                Direction.W,
                (random.nextDouble(
                    quadTreeRectangle.getFirstPoint().lengthCoordinate(),
                    quadTreeRectangle.getSecondPoint().lengthCoordinate())));
          IntegerShape data = new IntegerShape(i, new Rectangle(firstPointOfItem, secondPointOfItem));
          quadTree.insert(data);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.printf("%d. iteration, Duration: %d%n", numItems, duration);

        //                System.out.println("Inserted " + numItems + " items in " + duration + "
        // milliseconds.");
        csvWriter
            .append(String.valueOf(numItems))
            .append(",")
            .append(String.valueOf(duration))
            .append("\n");
      }

      csvWriter.flush();
      csvWriter.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
