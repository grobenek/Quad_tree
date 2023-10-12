package quadtree;

import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuadNode<T> {
  public static final int MAX_CHILDREN = 4;
  private final QuadNode<T>[] children;
  private final Rectangle shape;
  private final List<T> items;
  public QuadNode(Rectangle shape) {
    this.children = (QuadNode<T>[]) new QuadNode[MAX_CHILDREN]; // TODO warning
    this.shape = shape;
    this.items = new ArrayList<>();
  }

  public QuadNode(T data, Rectangle shape) {
    this.children = (QuadNode<T>[]) new QuadNode[MAX_CHILDREN]; // TODO warning
    this.shape = shape;
    this.items = new ArrayList<>();
    items.add(data);
  }

  public T getItem(int index) {
    return items.get(index);
  }

  public T getItem(T item) {
    int indexOfFirstItem = items.indexOf(item);

    return indexOfFirstItem != -1 ? items.get(indexOfFirstItem) : null;
  }

  public void addItem(T item) {
    items.add(item);
  }

  public int getItemsSize() {
    return items.size();
  }

  public List<T> getAllItems() {
    return items;
  }

  public Rectangle getShape() {
    return shape;
  }

  public QuadNode<T>[] getChildren() {
    return children;
  }

  public QuadNode<T> getChild(Quadrant quadrant) {
    if (quadrant == null) {
      throw new IllegalArgumentException("Cannot get child, because null is not valid direction!");
    }

    return children[quadrant.ordinal()];
  }

  private void addChild(QuadNode<T> child, Quadrant quadrant) {
    checkForExistingChildrenAtDirection(quadrant);

    children[quadrant.ordinal()] = child;
  }

  public void generateChild(Quadrant quadrant) {
    checkForExistingChildrenAtDirection(quadrant);

    addChild(new QuadNode<>(generateChildShape(quadrant)), quadrant);
  }

  private void checkForExistingChildrenAtDirection(Quadrant quadrant) {
    if (this.children[quadrant.ordinal()] != null) {
      throw new IllegalStateException(
          String.format(
              "Child at direction %d already exists at [%f, %f], [%f, %f]!",
              quadrant.ordinal(),
              shape.getFirstPoint().widthCoordinate(),
              shape.getFirstPoint().lengthCoordinate(),
              shape.getSecondPoint().widthCoordinate(),
              shape.getSecondPoint().lengthCoordinate()));
    }
  }

  private Rectangle generateChildShape(Quadrant quadrant) {
    GpsCoordinates bottomLeft = shape.getFirstPoint();
    GpsCoordinates topRight = shape.getSecondPoint();

    double midWidth = shape.getHalfWidth();
    double midLength = shape.getHalfLength();

    switch (quadrant) {
      case NORTH_WEST -> {
        return new Rectangle(
            new GpsCoordinates(Direction.S, bottomLeft.widthCoordinate(), Direction.W, midLength),
            new GpsCoordinates(Direction.N, midWidth, Direction.E, topRight.lengthCoordinate()));
      }
      case NORTH_EAST -> {
        return new Rectangle(
            new GpsCoordinates(Direction.S, midWidth, Direction.W, midLength),
            new GpsCoordinates(
                Direction.N, topRight.widthCoordinate(), Direction.E, topRight.lengthCoordinate()));
      }
      case SOUTH_WEST -> {
        return new Rectangle(
            new GpsCoordinates(
                Direction.S,
                bottomLeft.widthCoordinate(),
                Direction.W,
                bottomLeft.lengthCoordinate()),
            new GpsCoordinates(Direction.N, midWidth, Direction.E, midLength));
      }
      case SOUTH_EAST -> {
        return new Rectangle(
            new GpsCoordinates(Direction.S, midWidth, Direction.W, bottomLeft.lengthCoordinate()),
            new GpsCoordinates(Direction.N, topRight.widthCoordinate(), Direction.E, midLength));
      }
      default -> throw new IllegalArgumentException(
          String.format("Invalid direction %d in generateChildShape", quadrant.ordinal()));
    }
  }

  public int getChildrenSize() {
    int notNullChildrenCounter = 0;

    for (QuadNode<T> child : children) {
      if (child != null) {
        notNullChildrenCounter++;
      }
    }

    return notNullChildrenCounter;
  }

  /**
   * Checks for shape presence in quadrant. Returns null if it is in multiple quadrants
   *
   * @param shapeToChooseQuadrantFor shape to check
   * @return quadrant where shape is in or null if it is in multiple quadrants
   */
  public Quadrant getQuadrantOfShape(Rectangle shapeToChooseQuadrantFor) {
    GpsCoordinates bottomLeftPoint = shapeToChooseQuadrantFor.getFirstPoint();
    GpsCoordinates topRightPoint = shapeToChooseQuadrantFor.getSecondPoint();

    if (chekForPresenceInNorthWestQuadrant(bottomLeftPoint, topRightPoint)) {
      return Quadrant.NORTH_WEST;
    }
    if (checkForPresenceInNorthEastQuadrant(bottomLeftPoint, topRightPoint)) {
      return Quadrant.NORTH_EAST;
    }
    if (checkForPresenceInSouthWestQuadrant(bottomLeftPoint, topRightPoint)) {
      return Quadrant.SOUTH_WEST;
    }
    if (checkForPresenceInSouthEastQuadrant(bottomLeftPoint, topRightPoint)) {
      return Quadrant.SOUTH_EAST;
    }
    return null;
  }

  private boolean checkForPresenceInSouthEastQuadrant(
      GpsCoordinates bottomLeftPoint, GpsCoordinates topRightPoint) {
    return ((bottomLeftPoint.widthCoordinate() > shape.getHalfWidth()
            && bottomLeftPoint.widthCoordinate() < shape.getSecondPoint().widthCoordinate()
            && (bottomLeftPoint.lengthCoordinate() > shape.getFirstPoint().lengthCoordinate()
                && bottomLeftPoint.lengthCoordinate() < shape.getHalfLength()))
        && (topRightPoint.widthCoordinate() > shape.getHalfWidth()
            && topRightPoint.widthCoordinate() < shape.getSecondPoint().widthCoordinate()
            && (topRightPoint.lengthCoordinate() > shape.getFirstPoint().lengthCoordinate()
                && topRightPoint.lengthCoordinate() < shape.getHalfLength())));
  }

  private boolean checkForPresenceInSouthWestQuadrant(
      GpsCoordinates bottomLeftPoint, GpsCoordinates topRightPoint) {
    return ((bottomLeftPoint.widthCoordinate() > shape.getFirstPoint().widthCoordinate()
            && bottomLeftPoint.widthCoordinate() < shape.getHalfWidth()
            && (bottomLeftPoint.lengthCoordinate() > shape.getFirstPoint().lengthCoordinate()
                && bottomLeftPoint.lengthCoordinate() < shape.getHalfLength()))
        && (topRightPoint.widthCoordinate() > shape.getFirstPoint().widthCoordinate()
            && topRightPoint.widthCoordinate() < shape.getHalfWidth()
            && (topRightPoint.lengthCoordinate() > shape.getFirstPoint().lengthCoordinate()
                && topRightPoint.lengthCoordinate() < shape.getHalfLength())));
  }

  private boolean checkForPresenceInNorthEastQuadrant(
      GpsCoordinates bottomLeftPoint, GpsCoordinates topRightPoint) {
    return ((bottomLeftPoint.widthCoordinate() > shape.getHalfWidth()
                && bottomLeftPoint.widthCoordinate() < shape.getSecondPoint().widthCoordinate())
            && (bottomLeftPoint.lengthCoordinate() > shape.getHalfLength()
                && bottomLeftPoint.lengthCoordinate() < shape.getSecondPoint().lengthCoordinate()))
        && ((topRightPoint.widthCoordinate() > shape.getHalfWidth()
                && topRightPoint.widthCoordinate() < shape.getSecondPoint().widthCoordinate())
            && (topRightPoint.lengthCoordinate() > shape.getHalfLength()
                && topRightPoint.lengthCoordinate() < shape.getSecondPoint().lengthCoordinate()));
  }

  private boolean chekForPresenceInNorthWestQuadrant(
      GpsCoordinates bottomLeftPoint, GpsCoordinates topRightPoint) {
    return ((bottomLeftPoint.widthCoordinate() > shape.getFirstPoint().widthCoordinate()
                && bottomLeftPoint.widthCoordinate() < shape.getHalfWidth())
            && (bottomLeftPoint.lengthCoordinate() > shape.getHalfLength()
                && bottomLeftPoint.lengthCoordinate() < shape.getSecondPoint().lengthCoordinate()))
        && ((topRightPoint.widthCoordinate() > shape.getFirstPoint().widthCoordinate()
                && topRightPoint.widthCoordinate() < shape.getHalfWidth())
            && (topRightPoint.lengthCoordinate() > shape.getHalfLength()
                && topRightPoint.lengthCoordinate() < shape.getSecondPoint().lengthCoordinate()));
  }

  @Override
  public String toString() {
    return "QuadNode{"
        + "children="
        + Arrays.toString(children)
        + ", gpsCoordinates="
        + shape
        + ", items="
        + items
        + '}';
  }
}
