package test.quadtree;

import static org.junit.jupiter.api.Assertions.*;

import entity.Property;
import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import quadtree.QuadTree;

public class QuadTreeTest {
  public static final int NUMBER_OF_REPETITIONS = 3;
  public static final int NUMBER_OF_ITEMS_FOR_ACTIONS = 15000;

  @Test
  void insertDataThatNotFit() {
    QuadTree<Property> quadTree =
        new QuadTree<>(
            100,
            new Rectangle(
                new GpsCoordinates(Direction.S, 0, Direction.W, 0),
                new GpsCoordinates(Direction.N, 10, Direction.E, 10)));

    Property notFittingProperty1X =
        new Property(
            1,
            "",
            new Rectangle(
                new GpsCoordinates(Direction.S, 11, Direction.W, 1),
                new GpsCoordinates(Direction.N, 2, Direction.E, 2)));

    assertThrowsExactly(
        IllegalArgumentException.class, () -> quadTree.insert(notFittingProperty1X));

    Property notFittingProperty1Y =
        new Property(
            1,
            "",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1, Direction.W, 11),
                new GpsCoordinates(Direction.N, 2, Direction.E, 2)));

    assertThrowsExactly(
        IllegalArgumentException.class, () -> quadTree.insert(notFittingProperty1Y));

    Property notFittingProperty2X =
        new Property(
            1,
            "",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1, Direction.W, 1),
                new GpsCoordinates(Direction.N, 22, Direction.E, 2)));

    assertThrowsExactly(
        IllegalArgumentException.class, () -> quadTree.insert(notFittingProperty2X));

    Property notFittingProperty2Y =
        new Property(
            1,
            "",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1, Direction.W, 1),
                new GpsCoordinates(Direction.N, 2, Direction.E, 22)));

    assertThrowsExactly(
        IllegalArgumentException.class, () -> quadTree.insert(notFittingProperty2Y));
  }

  @Test
  void verifyNoDataLossWhenInserting() {
    Random random = new Random();
    for (int repetetion = 0; repetetion < NUMBER_OF_REPETITIONS; repetetion++) {
      List<Property> insertedItems = new ArrayList<>();

      int maxHeight = random.nextInt(1000);
      GpsCoordinates firstPoint =
          new GpsCoordinates(
              Direction.S, random.nextDouble(1000), Direction.W, random.nextDouble(1000));
      GpsCoordinates secondPoint =
          new GpsCoordinates(
              Direction.S, random.nextDouble(1000), Direction.W, random.nextDouble(1000));

      Rectangle searchAllItemsRectangle = new Rectangle(firstPoint, secondPoint);

      QuadTree<Property> quadTree =
          new QuadTree<>(maxHeight, new Rectangle(firstPoint, secondPoint));

      insertAndTestInsertedItems(random, insertedItems, searchAllItemsRectangle, quadTree);
    }
  }

  @Test
  void deleteItems() {
    QuadTree<Property> quadTree =
        new QuadTree<>(
            100,
            new Rectangle(
                new GpsCoordinates(Direction.S, 0, Direction.W, 0),
                new GpsCoordinates(Direction.N, 10, Direction.E, 10)));

    Rectangle findAllDataRectangle =
        new Rectangle(
            new GpsCoordinates(Direction.S, 0, Direction.W, 0),
            new GpsCoordinates(Direction.N, 10, Direction.E, 10));

    Property data1 =
        new Property(
            1,
            "data1",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1, Direction.W, 1),
                new GpsCoordinates(Direction.N, 2, Direction.E, 2)));

    Property data2 =
        new Property(
            2,
            "data2",
            new Rectangle(
                new GpsCoordinates(Direction.S, 5, Direction.W, 5),
                new GpsCoordinates(Direction.N, 2, Direction.E, 8)));

    assertDoesNotThrow(() -> quadTree.insert(data1));
    assertDoesNotThrow(() -> quadTree.insert(data2));

    assertEquals(2, quadTree.search(findAllDataRectangle).size());

    assertDoesNotThrow(() -> quadTree.deleteData(data1));

    assertEquals(1, quadTree.search(findAllDataRectangle).size());

    assertThrows(IllegalStateException.class, () -> quadTree.deleteData(data1));

    assertEquals(1, quadTree.search(findAllDataRectangle).size());
  }

  @Test
  void verifyNoDataLossWhenDeleting() {
    Random random = new Random();
    List<Property> insertedItems = new ArrayList<>();

    int maxHeight = random.nextInt(1000);
    GpsCoordinates firstPoint =
        new GpsCoordinates(
            Direction.S, random.nextDouble(1000), Direction.W, random.nextDouble(1000));
    GpsCoordinates secondPoint =
        new GpsCoordinates(
            Direction.S, random.nextDouble(1000), Direction.W, random.nextDouble(1000));

    Rectangle searchAllItemsRectangle = new Rectangle(firstPoint, secondPoint);

    QuadTree<Property> quadTree = new QuadTree<>(maxHeight, new Rectangle(firstPoint, secondPoint));

    for (int i = 0; i < NUMBER_OF_REPETITIONS; i++) {
      insertAndTestInsertedItems(random, insertedItems, searchAllItemsRectangle, quadTree);

      AtomicReference<List<Property>> listOfAllDataInQuadTree =
          new AtomicReference<>(); // must be AtomicReference because value is assigned in lambda
      assertDoesNotThrow(
          () -> listOfAllDataInQuadTree.set(quadTree.search(searchAllItemsRectangle)));

      assertFalse(listOfAllDataInQuadTree.get().isEmpty());
      assertEquals(listOfAllDataInQuadTree.get().size(), insertedItems.size());

      int numberOfItemsToRemove = random.nextInt(NUMBER_OF_ITEMS_FOR_ACTIONS);
      for (int j = 0; j < numberOfItemsToRemove; j++) {
        int indexOfItemToRemove = random.nextInt(insertedItems.size());

        assertDoesNotThrow(
            () -> listOfAllDataInQuadTree.set(quadTree.search(searchAllItemsRectangle)));

        Property propertyToRemove = insertedItems.get(indexOfItemToRemove);
        assertNotNull(propertyToRemove);
        assertDoesNotThrow(() -> quadTree.deleteData(propertyToRemove));

        assertEquals(listOfAllDataInQuadTree.get().size() - 1, insertedItems.size() - 1);

        assertFalse(quadTree.search(propertyToRemove.getShapeOfData()).contains(propertyToRemove));

        insertedItems.remove(indexOfItemToRemove);
      }
    }
  }

  private void insertAndTestInsertedItems(
      Random random,
      List<Property> insertedItems,
      Rectangle searchAllItemsRectangle,
      QuadTree<Property> quadTree) {
    for (int j = 0; j < NUMBER_OF_ITEMS_FOR_ACTIONS; j++) {
      GpsCoordinates firstPointOfItem =
          new GpsCoordinates(
              Direction.S,
              (random.nextDouble(
                  searchAllItemsRectangle.getFirstPoint().widthCoordinate(),
                  searchAllItemsRectangle.getSecondPoint().widthCoordinate())),
              Direction.W,
              (random.nextDouble(
                  searchAllItemsRectangle.getFirstPoint().lengthCoordinate(),
                  searchAllItemsRectangle.getSecondPoint().lengthCoordinate())));
      GpsCoordinates secondPointOfItem =
          new GpsCoordinates(
              Direction.S,
              (random.nextDouble(
                  searchAllItemsRectangle.getFirstPoint().widthCoordinate(),
                  searchAllItemsRectangle.getSecondPoint().widthCoordinate())),
              Direction.W,
              (random.nextDouble(
                  searchAllItemsRectangle.getFirstPoint().lengthCoordinate(),
                  searchAllItemsRectangle.getSecondPoint().lengthCoordinate())));

      Property itemToInsert =
          new Property(j, String.valueOf(j), new Rectangle(firstPointOfItem, secondPointOfItem));
      insertedItems.add(itemToInsert);

      assertDoesNotThrow(() -> quadTree.insert(itemToInsert));

      AtomicReference<List<Property>> listOfAllDataInQuadTree =
          new AtomicReference<>(); // must be AtomicReference because value is assigned in lambda
      assertDoesNotThrow(
          () -> listOfAllDataInQuadTree.set(quadTree.search(searchAllItemsRectangle)));

      assertFalse(listOfAllDataInQuadTree.get().isEmpty());
      assertEquals(listOfAllDataInQuadTree.get().size(), insertedItems.size());

      assertTrue(
          quadTree
              .search(new Rectangle(firstPointOfItem, secondPointOfItem))
              .contains(insertedItems.get(insertedItems.size() - 1)));
    }
  }
}
