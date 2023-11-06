package entity.shape;

import quadtree.IShapeData;

public class Rectangle implements IShapeData {
  private final GpsCoordinates firstPoint;
  private final GpsCoordinates secondPoint;
  private final double width;
  private final double length;

  private final double halfWidth;
  private final double halfLength;

  public Rectangle(GpsCoordinates firstPoint, GpsCoordinates secondPoint) {
    this.firstPoint =
        new GpsCoordinates(
            Direction.S,
            Math.min(firstPoint.widthCoordinate(), secondPoint.widthCoordinate()),
            Direction.W,
            Math.min(firstPoint.lengthCoordinate(), secondPoint.lengthCoordinate()));
    this.secondPoint =
        new GpsCoordinates(
            Direction.S,
            Math.max(firstPoint.widthCoordinate(), secondPoint.widthCoordinate()),
            Direction.W,
            Math.max(firstPoint.lengthCoordinate(), secondPoint.lengthCoordinate()));

    this.width = Math.abs(firstPoint.widthCoordinate() - secondPoint.widthCoordinate());
    this.length = Math.abs(firstPoint.lengthCoordinate() - secondPoint.lengthCoordinate());

    this.halfWidth = (this.firstPoint.widthCoordinate() + this.secondPoint.widthCoordinate()) / 2;
    this.halfLength =
        (this.firstPoint.lengthCoordinate() + this.secondPoint.lengthCoordinate()) / 2;
  }

  public GpsCoordinates getFirstPoint() {
    return firstPoint;
  }

  public GpsCoordinates getSecondPoint() {
    return secondPoint;
  }

  public double getWidth() {
    return width;
  }

  public double getLength() {
    return length;
  }

  public double getHalfWidth() {
    return halfWidth;
  }

  public double getHalfLength() {
    return halfLength;
  }

  public boolean isPoint() {
    return firstPoint.equals(secondPoint);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Rectangle)) {
      return false;
    }
    Rectangle castedObj = (Rectangle) obj;

    return (castedObj.getFirstPoint().equals(firstPoint)
        && castedObj.getSecondPoint().equals(secondPoint));
  }

  @Override
  public String toString() {
    return "Rectangle{"
        + "firstPoint="
        + String.format("[%f, %f]", firstPoint.widthCoordinate(), firstPoint.lengthCoordinate())
        + ", secondPoint="
        + String.format("[%f, %f]", secondPoint.widthCoordinate(), secondPoint.lengthCoordinate())
        + ", width="
        + width
        + ", length="
        + length
        + "}\n";
  }

  @Override
  public Rectangle getShapeOfData() {
    return this;
  }

  public boolean doesOverlapWithRectangle(Rectangle otherRectangle) {
    double leftWidth = firstPoint.widthCoordinate();
    double rightWidth = secondPoint.widthCoordinate();
    double bottomLength = firstPoint.lengthCoordinate();
    double topLength = secondPoint.lengthCoordinate();

    double otherLeft = otherRectangle.getFirstPoint().widthCoordinate();
    double otherRight = otherRectangle.getSecondPoint().widthCoordinate();
    double otherTop = otherRectangle.getFirstPoint().lengthCoordinate();
    double otherBottom = otherRectangle.getSecondPoint().lengthCoordinate();

    return (leftWidth < otherRight && rightWidth > otherLeft && bottomLength < otherBottom && topLength > otherTop);
  }
}
