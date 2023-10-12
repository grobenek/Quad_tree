package entity.shape;

public class Rectangle {
  private final GpsCoordinates firstPoint;
  private final GpsCoordinates secondPoint;
  private final double width;
  private final double length;

  private final double halfWidth;
  private final double halfLength;

  public Rectangle(GpsCoordinates firstPoint, GpsCoordinates secondPoint) {
    this.firstPoint = firstPoint;
    this.secondPoint = secondPoint;

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
        + '}';
  }
}
