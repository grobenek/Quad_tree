package test.shape;

import static org.junit.jupiter.api.Assertions.*;

import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;

class RectangleTest {

  private Rectangle testSquare;
  private Rectangle testPoint;
  private Rectangle testRectangle;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    GpsCoordinates squareBottomLeft = new GpsCoordinates(Direction.S, 0, Direction.W, 0);
    GpsCoordinates squareTopRightPoint = new GpsCoordinates(Direction.N, 10, Direction.E, 10);
    testSquare = new Rectangle(squareBottomLeft, squareTopRightPoint);

    GpsCoordinates pointBottomLeft = new GpsCoordinates(Direction.S, 1, Direction.W, 1);
    GpsCoordinates pointTopRight = new GpsCoordinates(Direction.N, 1, Direction.E, 1);
    testPoint = new Rectangle(pointBottomLeft, pointTopRight);

    GpsCoordinates rectangleBottomLeft = new GpsCoordinates(Direction.S, 2, Direction.W, 2);
    GpsCoordinates rectangleTopRight = new GpsCoordinates(Direction.N, 4, Direction.E, 5);
    testRectangle = new Rectangle(rectangleBottomLeft, rectangleTopRight);
  }

  @org.junit.jupiter.api.Test
  void getWidthOfSquare() {
    assertNotNull(testSquare);
    assertEquals(10, testSquare.getWidth());
  }

  @org.junit.jupiter.api.Test
  void getLengthOfSquare() {
    assertNotNull(testSquare);
    assertEquals(10, testSquare.getLength());
  }

  @org.junit.jupiter.api.Test
  void getWidthOfPoint() {
    assertNotNull(testPoint);
    assertEquals(0, testPoint.getWidth());
  }

  @org.junit.jupiter.api.Test
  void getLengthOfPoint() {
    assertNotNull(testSquare);
    assertEquals(0, testPoint.getLength());
  }

  @org.junit.jupiter.api.Test
  void getWidthOfRectangle() {
    assertNotNull(testRectangle);
    assertEquals(2, testRectangle.getWidth());
  }

  @org.junit.jupiter.api.Test
  void getLengthOfRectangle() {
    assertNotNull(testRectangle);
    assertEquals(3, testRectangle.getLength());
  }
}
