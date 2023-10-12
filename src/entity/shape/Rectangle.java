package entity.shape;

public class Rectangle {
  private final GpsCoordinates firstPoint;
  private final GpsCoordinates secondPoint;
  private final double width;
  private final double length;

  public Rectangle(GpsCoordinates firstPoint, GpsCoordinates secondPoint) {
    this.firstPoint = firstPoint;
    this.secondPoint = secondPoint;

    this.width = Math.abs(firstPoint.widthCoordinate() - secondPoint.widthCoordinate());
    this.length = Math.abs(firstPoint.lengthCoordinate() - secondPoint.lengthCoordinate());
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

  @Override
  public String toString() {
    return "Rectangle{" +
            "firstPoint=" + String.format("[%f, %f]", firstPoint.widthCoordinate(), firstPoint.lengthCoordinate()) +
            ", secondPoint=" + String.format("[%f, %f]", secondPoint.widthCoordinate(), secondPoint.lengthCoordinate()) +
            ", width=" + width +
            ", length=" + length +
            '}';
  }
}
