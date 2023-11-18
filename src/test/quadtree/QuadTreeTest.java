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
      List<Property> insertedItems = new ArrayList<>(NUMBER_OF_ITEMS_FOR_ACTIONS);

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

    for (int i = 0; i < NUMBER_OF_REPETITIONS; i++) {
      List<Property> insertedItems = new ArrayList<>(NUMBER_OF_ITEMS_FOR_ACTIONS);

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
        assertTrue(listOfAllDataInQuadTree.get().contains(propertyToRemove));
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

  @Test
  void generateAndTestMethodsOfQuadTree() {
    int searchChanceIn100 = 20;
    int insertChanceIn100 = 50;
    int deleteChanceIn100 = 30;

    if ((searchChanceIn100 + insertChanceIn100 + deleteChanceIn100) != 100) {
      throw new IllegalStateException(
          String.format(
              "Probabilities are not correctly assigned! Their sum is: %d",
              searchChanceIn100 + insertChanceIn100 + deleteChanceIn100));
    }

    Random random = new Random();

    for (int repetetion = 0; repetetion < NUMBER_OF_REPETITIONS; repetetion++) {
      int maxHeight = random.nextInt(1000);
      GpsCoordinates firstPoint =
          new GpsCoordinates(
              Direction.S, random.nextDouble(1000), Direction.W, random.nextDouble(1000));
      GpsCoordinates secondPoint =
          new GpsCoordinates(
              Direction.S, random.nextDouble(1000), Direction.W, random.nextDouble(1000));

      Rectangle searchAllItemsRectangle = new Rectangle(firstPoint, secondPoint);

      QuadTree<Property> quadTree = new QuadTree<>(maxHeight, searchAllItemsRectangle);
      List<Property> insertedItems = new ArrayList<>(NUMBER_OF_ITEMS_FOR_ACTIONS);

      int searchCounter = 0;
      int insertCounter = 0;
      int deleteCounter = 0;
      for (int action = 0; action < NUMBER_OF_ITEMS_FOR_ACTIONS; action++) {
        int chance = random.nextInt(101);

        if (chance <= searchChanceIn100) {
          seachAndTestResult(random, insertedItems, searchAllItemsRectangle, quadTree);
          assertEquals(quadTree.getSize(), insertedItems.size());
          searchCounter++;
        } else if (chance <= searchChanceIn100 + deleteChanceIn100) {
          deleteAndTestResult(random, insertedItems, searchAllItemsRectangle, quadTree);
          assertEquals(quadTree.getSize(), insertedItems.size());
          deleteCounter++;
        } else {
          insertAndTestResult(random, insertedItems, searchAllItemsRectangle, quadTree);
          assertEquals(quadTree.getSize(), insertedItems.size());
          insertCounter++;
        }
      }
      System.out.printf(
          "%d. repetition: %.2f%% search, %.2f%% delete, %.2f%% insert\n",
          repetetion + 1,
          ((double) searchCounter / NUMBER_OF_ITEMS_FOR_ACTIONS) * 100,
          ((double) deleteCounter / NUMBER_OF_ITEMS_FOR_ACTIONS) * 100,
          ((double) insertCounter / NUMBER_OF_ITEMS_FOR_ACTIONS) * 100);
      System.out.printf(
          "NORTH_EAST: %d | NORTH_WEST: %d | SOUTH_EAST: %d | SOUTH_WEST: %d | ROOT: %d\n",
          quadTree.getItemsInNorthEast(),
          quadTree.getItemsInNorthWest(),
          quadTree.getItemsInSouthEast(),
          quadTree.getItemsInSouthWest(),
          quadTree.getItemsInRoot());

      System.out.printf("Health of quadTree: %.02f\n\n", quadTree.getHealthOfQuadTree());
      System.out.printf("Size of tree: %d\n", quadTree.getSize());
    }
  }

  private void insertAndTestResult(
      Random random,
      List<Property> insertedItems,
      Rectangle searchAllItemsRectangle,
      QuadTree<Property> quadTree) {
    assertNotNull(random);
    assertNotNull(quadTree);
    assertNotNull(searchAllItemsRectangle);

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
        new Property(
            insertedItems.size(),
            String.valueOf(insertedItems.size()),
            new Rectangle(firstPointOfItem, secondPointOfItem));
    insertedItems.add(itemToInsert);

    assertDoesNotThrow(() -> quadTree.insert(itemToInsert));

    AtomicReference<List<Property>> listOfAllDataInQuadTree =
        new AtomicReference<>(); // must be AtomicReference because value is assigned in lambda
    assertDoesNotThrow(() -> listOfAllDataInQuadTree.set(quadTree.search(searchAllItemsRectangle)));

    assertFalse(listOfAllDataInQuadTree.get().isEmpty());
    assertEquals(listOfAllDataInQuadTree.get().size(), insertedItems.size());

    assertTrue(
        quadTree
            .search(new Rectangle(firstPointOfItem, secondPointOfItem))
            .contains(insertedItems.get(insertedItems.size() - 1)));
  }

  private void deleteAndTestResult(
      Random random,
      List<Property> insertedItems,
      Rectangle searchAllItemsRectangle,
      QuadTree<Property> quadTree) {
    assertNotNull(random);
    assertNotNull(quadTree);
    assertNotNull(searchAllItemsRectangle);

    if (insertedItems.isEmpty()) {
      assertThrows(
          IllegalStateException.class,
          () -> quadTree.deleteData(new Property(1, "1", searchAllItemsRectangle)));
      return;
    }

    int indexOfItemToDelete = random.nextInt(insertedItems.size());
    Property itemToDelete = assertDoesNotThrow(() -> insertedItems.get(indexOfItemToDelete));
    assertNotNull(itemToDelete);

    List<Property> foundAllItems = quadTree.search(searchAllItemsRectangle);
    assertEquals(insertedItems.size(), foundAllItems.size());
    assertTrue(insertedItems.contains(itemToDelete));

    assertDoesNotThrow(() -> quadTree.deleteData(itemToDelete));
    insertedItems.remove(indexOfItemToDelete);

    assertThrows(IllegalStateException.class, () -> quadTree.deleteData(itemToDelete));
    assertThrows(IllegalArgumentException.class, () -> quadTree.deleteData(null));

    assertEquals(insertedItems.size(), foundAllItems.size() - 1);

    foundAllItems = quadTree.search(searchAllItemsRectangle);
    assertEquals(insertedItems.size(), foundAllItems.size());
    assertFalse(insertedItems.contains(itemToDelete));
  }

  private void seachAndTestResult(
      Random random,
      List<Property> insertedItems,
      Rectangle searchAllItemsRectangle,
      QuadTree<Property> quadTree) {
    assertNotNull(random);
    assertNotNull(quadTree);
    assertNotNull(searchAllItemsRectangle);

    if (insertedItems.isEmpty()) {
      assertTrue(quadTree.search(searchAllItemsRectangle).isEmpty());
      return;
    }

    Property itemToSearchFor =
        assertDoesNotThrow(() -> insertedItems.get(random.nextInt(insertedItems.size())));
    assertNotNull(itemToSearchFor);

    List<Property> foundAllItems = quadTree.search(searchAllItemsRectangle);
    assertEquals(insertedItems.size(), foundAllItems.size());
    assertTrue(insertedItems.contains(itemToSearchFor));

    List<Property> foundItemsWhenSearchingExactItem =
        quadTree.search(itemToSearchFor.getShapeOfData());
    assertNotNull(foundItemsWhenSearchingExactItem);
    assertTrue(insertedItems.containsAll(foundItemsWhenSearchingExactItem));
    assertTrue(foundItemsWhenSearchingExactItem.contains(itemToSearchFor));
  }

  @Test
  void checkGoodQuadTreeHealth() {
    QuadTree<Property> quadTree =
        new QuadTree<>(
            10,
            new Rectangle(
                new GpsCoordinates(Direction.S, 0.0, Direction.W, 0.0),
                new GpsCoordinates(Direction.N, 10.0, Direction.E, 10.0)));

    Property southEastProperty =
        new Property(
            0,
            "0",
            new Rectangle(
                new GpsCoordinates(Direction.S, 6.0, Direction.W, 1.0),
                new GpsCoordinates(Direction.N, 7.0, Direction.E, 1.0)));

    Property southWestProperty =
        new Property(
            1,
            "1",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1.0, Direction.W, 1.0),
                new GpsCoordinates(Direction.N, 2.0, Direction.E, 1.0)));

    Property northWestProperty =
        new Property(
            2,
            "2",
            new Rectangle(
                new GpsCoordinates(Direction.S, 6.0, Direction.W, 6.0),
                new GpsCoordinates(Direction.N, 6.0, Direction.E, 7.0)));

    Property northEastProperty =
        new Property(
            3,
            "3",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1.0, Direction.W, 6.0),
                new GpsCoordinates(Direction.N, 2.0, Direction.E, 6.0)));

    quadTree.insert(southEastProperty);
    quadTree.insert(southWestProperty);
    quadTree.insert(northEastProperty);
    quadTree.insert(northWestProperty);

    assertEquals(0.0, quadTree.getHealthOfQuadTree());
  }

  @Test
  void checkBadQuadTreeHealth() {
    QuadTree<Property> quadTree =
        new QuadTree<>(
            10,
            new Rectangle(
                new GpsCoordinates(Direction.S, 0.0, Direction.W, 0.0),
                new GpsCoordinates(Direction.N, 10.0, Direction.E, 10.0)));

    Property southEastProperty =
        new Property(
            0,
            "0",
            new Rectangle(
                new GpsCoordinates(Direction.S, 6.0, Direction.W, 7.0),
                new GpsCoordinates(Direction.N, 7.0, Direction.E, 4.0)));

    Property southWestProperty =
        new Property(
            1,
            "1",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1.0, Direction.W, 1.0),
                new GpsCoordinates(Direction.N, 2.0, Direction.E, 1.0)));

    Property northWestProperty =
        new Property(
            2,
            "2",
            new Rectangle(
                new GpsCoordinates(Direction.S, 7.0, Direction.W, 6.0),
                new GpsCoordinates(Direction.N, 6.0, Direction.E, 7.0)));

    Property northEastProperty =
        new Property(
            3,
            "3",
            new Rectangle(
                new GpsCoordinates(Direction.S, 4.0, Direction.W, 6.0),
                new GpsCoordinates(Direction.N, 2.0, Direction.E, 6.0)));

    quadTree.insert(southEastProperty);
    quadTree.insert(southWestProperty);
    quadTree.insert(northEastProperty);
    quadTree.insert(northWestProperty);

    assertNotEquals(0.0, quadTree.getHealthOfQuadTree());
  }

  @Test
  void setNewHeight() {
    Rectangle findAllRectangle =
        new Rectangle(
            new GpsCoordinates(Direction.S, 0.0, Direction.W, 0.0),
            new GpsCoordinates(Direction.N, 10.0, Direction.E, 10.0));

    List<Property> insertedItems = new ArrayList<>();

    QuadTree<Property> quadTree = new QuadTree<>(10, findAllRectangle);

    Property southEastProperty =
        new Property(
            0,
            "0",
            new Rectangle(
                new GpsCoordinates(Direction.S, 6.0, Direction.W, 1.0),
                new GpsCoordinates(Direction.N, 7.0, Direction.E, 1.0)));

    Property southWestProperty =
        new Property(
            1,
            "1",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1.0, Direction.W, 1.0),
                new GpsCoordinates(Direction.N, 2.0, Direction.E, 1.0)));

    Property northWestProperty =
        new Property(
            2,
            "2",
            new Rectangle(
                new GpsCoordinates(Direction.S, 6.0, Direction.W, 6.0),
                new GpsCoordinates(Direction.N, 6.0, Direction.E, 7.0)));

    Property northEastProperty =
        new Property(
            3,
            "3",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1.0, Direction.W, 6.0),
                new GpsCoordinates(Direction.N, 2.0, Direction.E, 6.0)));

    Property northWestProperty2 =
        new Property(
            4,
            "4",
            new Rectangle(
                new GpsCoordinates(Direction.S, 6.1, Direction.W, 6.1),
                new GpsCoordinates(Direction.N, 6.1, Direction.E, 7.1)));

    Property northEastProperty2 =
        new Property(
            5,
            "5",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1.1, Direction.W, 6.1),
                new GpsCoordinates(Direction.N, 2.1, Direction.E, 6.1)));
    Property northEastProperty3 =
        new Property(
            6,
            "6",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1.2, Direction.W, 7.1),
                new GpsCoordinates(Direction.N, 2.5, Direction.E, 6.1)));
    Property northEastProperty4 =
        new Property(
            7,
            "7",
            new Rectangle(
                new GpsCoordinates(Direction.S, 1.3, Direction.W, 7.2),
                new GpsCoordinates(Direction.N, 2.6, Direction.E, 6.2)));

    quadTree.insert(southEastProperty);
    quadTree.insert(southWestProperty);
    quadTree.insert(northEastProperty);
    quadTree.insert(northWestProperty);
    quadTree.insert(northEastProperty2);
    quadTree.insert(northWestProperty2);
    quadTree.insert(northEastProperty3);
    quadTree.insert(northEastProperty4);

    insertedItems.add(southEastProperty);
    insertedItems.add(southWestProperty);
    insertedItems.add(northEastProperty);
    insertedItems.add(northWestProperty);
    insertedItems.add(northEastProperty2);
    insertedItems.add(northWestProperty2);
    insertedItems.add(northEastProperty3);
    insertedItems.add(northEastProperty4);

    assertEquals(10, quadTree.getHeight());
    assertTrue(insertedItems.containsAll(quadTree.search(findAllRectangle)));

    quadTree.setHeight(2);
    assertEquals(2, quadTree.getHeight());
    assertTrue(insertedItems.containsAll(quadTree.search(findAllRectangle)));
  }

  void testOptimizationOnInsert() {
    Random random = new Random();
    int height = 100;
    GpsCoordinates firstPoint = new GpsCoordinates(Direction.S, 0, Direction.W, 0);
    GpsCoordinates secondPoint = new GpsCoordinates(Direction.N, 100, Direction.E, 100);
    Rectangle quadTreeRectangle = new Rectangle(firstPoint, secondPoint);

    QuadTree<Property> quadTree;

    int NUMBER_OF_ITEMS = 10500;

    List<Long> allTimesWithoutOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
    List<Long> allTimesWithOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
    for (int j = 0; j < 1000; j++) {
      List<Long> timesWithoutOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
      List<Long> timesWithOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
      List<Property> insertedItems = new ArrayList<>(NUMBER_OF_ITEMS);
      quadTree = new QuadTree<>(height, quadTreeRectangle);
      for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
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

        Property itemToInsert =
            new Property(i, String.valueOf(i), new Rectangle(firstPointOfItem, secondPointOfItem));

        long startTime = System.nanoTime();
        quadTree.insert(itemToInsert);
        long endTime = System.nanoTime();

        insertedItems.add(itemToInsert);
        timesWithoutOptimization.add(Math.abs(endTime - startTime));
      }

      quadTree = new QuadTree<>(height, quadTreeRectangle);

      for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
        Property itemToInsert = insertedItems.get(i);

        long startTime = System.nanoTime();
        quadTree.insert(itemToInsert);
        long endTime = System.nanoTime();
        timesWithOptimization.add(Math.abs(endTime - startTime));

        quadTree.optimize();
      }
      long averageTimeWitoutOptimization = 0;
      long averageTimeWithOptimization = 0;

      for (Long aLong : timesWithoutOptimization) {
        averageTimeWitoutOptimization += aLong;
      }

      for (Long aLong : timesWithOptimization) {
        averageTimeWithOptimization += aLong;
      }

      averageTimeWitoutOptimization =
          averageTimeWitoutOptimization / timesWithoutOptimization.size();
      averageTimeWithOptimization = averageTimeWithOptimization / timesWithOptimization.size();

      allTimesWithOptimization.add(averageTimeWitoutOptimization);
      allTimesWithoutOptimization.add(averageTimeWithOptimization);
    }

    long allAverageTimeWitoutOptimization = 0;
    long allAverageTimeWithOptimization = 0;

    for (Long aLong : allTimesWithoutOptimization) {
      allAverageTimeWitoutOptimization += aLong;
    }

    for (Long aLong : allTimesWithOptimization) {
      allAverageTimeWithOptimization += aLong;
    }

    allAverageTimeWitoutOptimization =
        allAverageTimeWitoutOptimization / allTimesWithoutOptimization.size();
    allAverageTimeWithOptimization =
        allAverageTimeWithOptimization / allTimesWithOptimization.size();

    System.out.println("Without optimization: " + allAverageTimeWitoutOptimization);
    System.out.println("With optimization: " + allAverageTimeWithOptimization);
  }

  @Test
  void testOptimizationOnSearch() {
    Random random = new Random();
    int height = 100;
    GpsCoordinates firstPoint = new GpsCoordinates(Direction.S, 0, Direction.W, 0);
    GpsCoordinates secondPoint = new GpsCoordinates(Direction.N, 100, Direction.E, 100);
    Rectangle quadTreeRectangle = new Rectangle(firstPoint, secondPoint);

    QuadTree<Property> quadTree;

    int NUMBER_OF_ITEMS = 10500;

    List<Long> allTimesWithoutOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
    List<Long> allTimesWithOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
    for (int j = 0; j < 50; j++) {
      List<Property> insertedItems = new ArrayList<>(NUMBER_OF_ITEMS);
      List<Property> insertedItemsBackup = new ArrayList<>(NUMBER_OF_ITEMS);
      List<Long> timesWithoutOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
      List<Long> timesWithOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
      quadTree = new QuadTree<>(height, quadTreeRectangle);
      for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
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

        Property itemToInsert =
            new Property(i, String.valueOf(i), new Rectangle(firstPointOfItem, secondPointOfItem));
        insertedItems.add(itemToInsert);
        insertedItemsBackup.add(itemToInsert);

        quadTree.insert(itemToInsert);
      }

      for (int i = 0; i < NUMBER_OF_ITEMS / 2; i++) {
        int randomIndex = random.nextInt(insertedItems.size());

        Property itemToSearch = insertedItems.get(randomIndex);

        long startTime = System.nanoTime();
        quadTree.search(itemToSearch.getShapeOfData());
        long endTime = System.nanoTime();

        timesWithoutOptimization.add(Math.abs(endTime - startTime));
      }

      quadTree = new QuadTree<>(height, quadTreeRectangle);
      insertedItems.clear();

      for (int i = 0; i < NUMBER_OF_ITEMS; i++) {

        Property itemToInsert = insertedItemsBackup.get(i);

        quadTree.insert(itemToInsert);
        quadTree.optimize();
      }

      for (int i = 0; i < NUMBER_OF_ITEMS / 2; i++) {
        int randomIndex = random.nextInt(insertedItemsBackup.size());

        Property itemToSearch = insertedItemsBackup.get(randomIndex);

        long startTime = System.nanoTime();
        quadTree.search(itemToSearch.getShapeOfData());
        long endTime = System.nanoTime();

        timesWithOptimization.add(Math.abs(endTime - startTime));
      }

      long averageTimeWithoutOptimization = 0;
      long averageTimeWithOptimization = 0;

      for (Long aLong : timesWithoutOptimization) {
        averageTimeWithoutOptimization += aLong;
      }

      for (Long aLong : timesWithOptimization) {
        averageTimeWithOptimization += aLong;
      }

      averageTimeWithoutOptimization =
          averageTimeWithoutOptimization / timesWithoutOptimization.size();
      averageTimeWithOptimization = averageTimeWithOptimization / timesWithOptimization.size();

      allTimesWithOptimization.add(averageTimeWithOptimization);
      allTimesWithoutOptimization.add(averageTimeWithoutOptimization);
    }

    long allAverageTimeWitoutOptimization = 0;
    long allAverageTimeWithOptimization = 0;

    for (Long aLong : allTimesWithoutOptimization) {
      allAverageTimeWitoutOptimization += aLong;
    }

    for (Long aLong : allTimesWithOptimization) {
      allAverageTimeWithOptimization += aLong;
    }

    allAverageTimeWitoutOptimization =
        allAverageTimeWitoutOptimization / allTimesWithoutOptimization.size();
    allAverageTimeWithOptimization =
        allAverageTimeWithOptimization / allTimesWithOptimization.size();

    System.out.println("Without optimization: " + allAverageTimeWitoutOptimization);
    System.out.println("With optimization: " + allAverageTimeWithOptimization);
  }

  void testOptimizationOnDelete() {
    Random random = new Random();
    int height = 100;
    GpsCoordinates firstPoint = new GpsCoordinates(Direction.S, 0, Direction.W, 0);
    GpsCoordinates secondPoint = new GpsCoordinates(Direction.N, 100, Direction.E, 100);
    Rectangle quadTreeRectangle = new Rectangle(firstPoint, secondPoint);

    QuadTree<Property> quadTree;

    int NUMBER_OF_ITEMS = 10500;

    List<Long> allTimesWithoutOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
    List<Long> allTimesWithOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
    for (int j = 0; j < 1000; j++) {
      List<Property> insertedItems = new ArrayList<>(NUMBER_OF_ITEMS);
      List<Property> insertedItemsBackup = new ArrayList<>(NUMBER_OF_ITEMS);
      List<Long> timesWithoutOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
      List<Long> timesWithOptimization = new ArrayList<>(NUMBER_OF_ITEMS);
      quadTree = new QuadTree<>(height, quadTreeRectangle);
      for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
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

        Property itemToInsert =
            new Property(i, String.valueOf(i), new Rectangle(firstPointOfItem, secondPointOfItem));
        insertedItems.add(itemToInsert);
        insertedItemsBackup.add(itemToInsert);

        quadTree.insert(itemToInsert);
      }

      for (int i = 0; i < NUMBER_OF_ITEMS / 2; i++) {
        int randomIndex = random.nextInt(insertedItems.size());

        Property itemToDelete = insertedItems.get(randomIndex);

        long startTime = System.nanoTime();
        quadTree.deleteData(itemToDelete);
        long endTime = System.nanoTime();

        timesWithoutOptimization.add(Math.abs(endTime - startTime));

        insertedItems.remove(randomIndex);
      }

      quadTree = new QuadTree<>(height, quadTreeRectangle);
      insertedItems.clear();

      for (int i = 0; i < NUMBER_OF_ITEMS; i++) {

        Property itemToInsert = insertedItemsBackup.get(i);

        quadTree.insert(itemToInsert);
        quadTree.optimize();
      }

      for (int i = 0; i < NUMBER_OF_ITEMS / 2; i++) {
        int randomIndex = random.nextInt(insertedItemsBackup.size());

        Property itemToDelete = insertedItemsBackup.get(randomIndex);

        long startTime = System.nanoTime();
        quadTree.deleteData(itemToDelete);
        long endTime = System.nanoTime();

        timesWithOptimization.add(Math.abs(endTime - startTime));

        quadTree.optimize();
        insertedItemsBackup.remove(randomIndex);
      }

      long averageTimeWithoutOptimization = 0;
      long averageTimeWithOptimization = 0;

      for (Long aLong : timesWithoutOptimization) {
        averageTimeWithoutOptimization += aLong;
      }

      for (Long aLong : timesWithOptimization) {
        averageTimeWithOptimization += aLong;
      }

      averageTimeWithoutOptimization =
          averageTimeWithoutOptimization / timesWithoutOptimization.size();
      averageTimeWithOptimization = averageTimeWithOptimization / timesWithOptimization.size();

      allTimesWithOptimization.add(averageTimeWithOptimization);
      allTimesWithoutOptimization.add(averageTimeWithoutOptimization);
    }

    long allAverageTimeWitoutOptimization = 0;
    long allAverageTimeWithOptimization = 0;

    for (Long aLong : allTimesWithoutOptimization) {
      allAverageTimeWitoutOptimization += aLong;
    }

    for (Long aLong : allTimesWithOptimization) {
      allAverageTimeWithOptimization += aLong;
    }

    allAverageTimeWitoutOptimization =
        allAverageTimeWitoutOptimization / allTimesWithoutOptimization.size();
    allAverageTimeWithOptimization =
        allAverageTimeWithOptimization / allTimesWithOptimization.size();

    System.out.println("Without optimization: " + allAverageTimeWitoutOptimization);
    System.out.println("With optimization: " + allAverageTimeWithOptimization);
  }
}
