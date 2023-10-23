package quadtree;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import entity.Property;
import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quadtree.QuadNode;
import quadtree.Quadrant;
import test.IntegerShape;

class QuadNodeTest {

  private QuadNode<IntegerShape> baseIntegerQuadNode;
  private QuadNode<Property> basePropertyQuadNode;
  private Rectangle quadNodeShape;

  @BeforeEach
  void setUp() {
    GpsCoordinates bottomLeftPoint = new GpsCoordinates(Direction.S, 0, Direction.W, 0);
    GpsCoordinates topRightPoint = new GpsCoordinates(Direction.N, 10, Direction.E, 10);
    quadNodeShape = new Rectangle(bottomLeftPoint, topRightPoint);
    baseIntegerQuadNode = new QuadNode<>(quadNodeShape);
    basePropertyQuadNode = new QuadNode<>(quadNodeShape);
  }

  @Test
  void getItem() {
    assertNotNull(baseIntegerQuadNode);
    assertNotNull(basePropertyQuadNode);

    for (int i = 0; i < 5; i++) {
      baseIntegerQuadNode.addItem(
          new IntegerShape(
              i,
              new Rectangle(
                  new GpsCoordinates(Direction.S, i, Direction.W, i),
                  new GpsCoordinates(Direction.S, i, Direction.W, i))));
    }

    assertEquals(0, baseIntegerQuadNode.getItem(0).getData());
    assertEquals(2, baseIntegerQuadNode.getItem(2).getData());
    assertEquals(4, baseIntegerQuadNode.getItem(4).getData());

    List<Property> propertyList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Property property = new Property(i, String.valueOf(i));
      propertyList.add(property);
      basePropertyQuadNode.addItem(property);
    }

    assertEquals(propertyList.get(0), basePropertyQuadNode.getItem(propertyList.get(0)));
    assertEquals(propertyList.get(2), basePropertyQuadNode.getItem(propertyList.get(2)));
  }

  @Test
  void addItemAndGetItemsSize() {
    assertNotNull(baseIntegerQuadNode);
    assertNotNull(basePropertyQuadNode);

    for (int i = 0; i < 5; i++) {
      Property property = new Property(i, String.valueOf(i));
      baseIntegerQuadNode.addItem(
          new IntegerShape(
              i,
              new Rectangle(
                  new GpsCoordinates(Direction.S, i, Direction.W, i),
                  new GpsCoordinates(Direction.S, i, Direction.W, i))));
      basePropertyQuadNode.addItem(property);
    }

    assertEquals(5, baseIntegerQuadNode.getItemsSize());
    assertEquals(5, basePropertyQuadNode.getItemsSize());
  }

  @Test
  void getAllItems() {
    assertNotNull(baseIntegerQuadNode);
    assertNotNull(basePropertyQuadNode);

    List<Property> propertyList = new ArrayList<>();
    List<Integer> integerList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Property property = new Property(i, String.valueOf(i));
      propertyList.add(property);
      integerList.add(i);
      baseIntegerQuadNode.addItem(
          new IntegerShape(
              i,
              new Rectangle(
                  new GpsCoordinates(Direction.S, i, Direction.W, i),
                  new GpsCoordinates(Direction.S, i, Direction.W, i))));
      basePropertyQuadNode.addItem(property);
    }

    assertEquals(propertyList, basePropertyQuadNode.getItems());
    assertEquals(
        integerList, baseIntegerQuadNode.getItems().stream().map(IntegerShape::getData).toList());
  }

  @Test
  void getShape() {
    assertNotNull(baseIntegerQuadNode);
    assertNotNull(basePropertyQuadNode);

    assertEquals(quadNodeShape, baseIntegerQuadNode.getShape());
    assertEquals(quadNodeShape, basePropertyQuadNode.getShape());
  }

  @Test
  void getChildren() {
    assertNotNull(baseIntegerQuadNode);

    baseIntegerQuadNode.generateChild(Quadrant.NORTH_WEST);
    baseIntegerQuadNode.generateChild(Quadrant.NORTH_EAST);

    QuadNode<IntegerShape>[] quadNodeChildren = baseIntegerQuadNode.getChildren();

    int counter = 0;
    for (QuadNode<IntegerShape> quadNodeChild : quadNodeChildren) {
      if (quadNodeChild != null) {
        counter++;
      }
    }

    assertEquals(2, counter);
  }

  @Test
  void getChild() {
    assertNotNull(baseIntegerQuadNode);

    baseIntegerQuadNode.generateChild(Quadrant.NORTH_WEST);
    baseIntegerQuadNode.generateChild(Quadrant.NORTH_EAST);

    assertNotNull(baseIntegerQuadNode.getChild(Quadrant.NORTH_WEST));
    assertNotNull(baseIntegerQuadNode.getChild(Quadrant.NORTH_EAST));

    assertNull(baseIntegerQuadNode.getChild(Quadrant.SOUTH_WEST));
    assertNull(baseIntegerQuadNode.getChild(Quadrant.SOUTH_EAST));

    assertThrows(IllegalArgumentException.class, () -> baseIntegerQuadNode.getChild(null));
  }

  @Test
  void generateChild() {
    assertNotNull(baseIntegerQuadNode);

    assertEquals(0, baseIntegerQuadNode.getChildrenSize());

    for (Quadrant quadrant : Quadrant.values()) {
      baseIntegerQuadNode.generateChild(quadrant);
      assertEquals(quadrant.ordinal() + 1, baseIntegerQuadNode.getChildrenSize());
    }

    assertTrue(
        List.of(0.0, 5.0, 5.0, 10.0)
            .containsAll(
                List.of(
                    baseIntegerQuadNode
                        .getChild(Quadrant.NORTH_WEST)
                        .getShape()
                        .getFirstPoint()
                        .widthCoordinate(),
                    baseIntegerQuadNode
                        .getChild(Quadrant.NORTH_WEST)
                        .getShape()
                        .getFirstPoint()
                        .lengthCoordinate(),
                    baseIntegerQuadNode
                        .getChild(Quadrant.NORTH_WEST)
                        .getShape()
                        .getSecondPoint()
                        .widthCoordinate(),
                    baseIntegerQuadNode
                        .getChild(Quadrant.NORTH_WEST)
                        .getShape()
                        .getSecondPoint()
                        .lengthCoordinate())));

    assertTrue(
        List.of(5.0, 0.0, 10.0, 5.0)
            .containsAll(
                List.of(
                    baseIntegerQuadNode
                        .getChild(Quadrant.SOUTH_EAST)
                        .getShape()
                        .getFirstPoint()
                        .widthCoordinate(),
                    baseIntegerQuadNode
                        .getChild(Quadrant.SOUTH_EAST)
                        .getShape()
                        .getFirstPoint()
                        .lengthCoordinate(),
                    baseIntegerQuadNode
                        .getChild(Quadrant.SOUTH_EAST)
                        .getShape()
                        .getSecondPoint()
                        .widthCoordinate(),
                    baseIntegerQuadNode
                        .getChild(Quadrant.SOUTH_EAST)
                        .getShape()
                        .getSecondPoint()
                        .lengthCoordinate())));

    QuadNode<IntegerShape> baseNodeNorthEastChild =
        baseIntegerQuadNode.getChild(Quadrant.NORTH_EAST);
    baseNodeNorthEastChild.generateChild(Quadrant.SOUTH_WEST);

    assertTrue(
        List.of(5.0, 5.0, 7.5, 7.5)
            .containsAll(
                List.of(
                    baseNodeNorthEastChild
                        .getChild(Quadrant.SOUTH_WEST)
                        .getShape()
                        .getFirstPoint()
                        .widthCoordinate(),
                    baseNodeNorthEastChild
                        .getChild(Quadrant.SOUTH_WEST)
                        .getShape()
                        .getFirstPoint()
                        .lengthCoordinate(),
                    baseNodeNorthEastChild
                        .getChild(Quadrant.SOUTH_WEST)
                        .getShape()
                        .getSecondPoint()
                        .widthCoordinate(),
                    baseNodeNorthEastChild
                        .getChild(Quadrant.SOUTH_WEST)
                        .getShape()
                        .getSecondPoint()
                        .lengthCoordinate())));

    assertThrows(
        IllegalStateException.class, () -> baseIntegerQuadNode.generateChild(Quadrant.NORTH_EAST));

    assertNull(baseIntegerQuadNode.getChild(Quadrant.NORTH_EAST).getChild(Quadrant.NORTH_EAST));
  }

  @Test
  void getQuadrantOfShape() {
    assertNotNull(baseIntegerQuadNode);

    Rectangle northWestShape =
        new Rectangle(
            new GpsCoordinates(Direction.S, 3.21, Direction.W, 9.99),
            new GpsCoordinates(Direction.N, 4.1, Direction.E, 6.156));

    IntegerShape northWestInteger = new IntegerShape(0, northWestShape);

    Rectangle northEastShape =
        new Rectangle(
            new GpsCoordinates(Direction.S, 8.641, Direction.W, 6.1),
            new GpsCoordinates(Direction.N, 9.99, Direction.E, 7.2));

    IntegerShape northEastInteger = new IntegerShape(0, northEastShape);

    Rectangle southWestShape =
        new Rectangle(
            new GpsCoordinates(Direction.S, 2.6, Direction.W, 2.43),
            new GpsCoordinates(Direction.N, 3.89, Direction.E, 1.5));

    IntegerShape southWestInteger = new IntegerShape(0, southWestShape);

    Rectangle southEastShape =
        new Rectangle(
            new GpsCoordinates(Direction.S, 6.1, Direction.W, 2.43),
            new GpsCoordinates(Direction.N, 9.99, Direction.E, 3.51221));

    IntegerShape southEastInteger = new IntegerShape(0, southEastShape);

    Rectangle multipleQuadrantsShape =
        new Rectangle(
            new GpsCoordinates(Direction.S, 2.5, Direction.W, 9.5),
            new GpsCoordinates(Direction.N, 9.99, Direction.E, 3.51221));

    IntegerShape multipleQuadrantsInteger = new IntegerShape(0, multipleQuadrantsShape);

    assertEquals(
        Quadrant.NORTH_WEST, baseIntegerQuadNode.getQuadrantOfShape(northWestInteger, false));
    assertEquals(
        Quadrant.NORTH_EAST, baseIntegerQuadNode.getQuadrantOfShape(northEastInteger, false));
    assertEquals(
        Quadrant.SOUTH_WEST, baseIntegerQuadNode.getQuadrantOfShape(southWestInteger, false));
    assertEquals(
        Quadrant.SOUTH_EAST, baseIntegerQuadNode.getQuadrantOfShape(southEastInteger, false));

    assertNull(baseIntegerQuadNode.getQuadrantOfShape(multipleQuadrantsInteger, false));
  }
}
