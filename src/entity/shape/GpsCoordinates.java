package entity.shape;

/**
 * @param width N or S
 * @param length E or W
 */
public record GpsCoordinates(
    Direction width, double widthCoordinate, Direction length, double lengthCoordinate) {

  @Override
  public String toString() {
    return "GpsCoordinates{"
        + "width="
        + width
        + ", widthCoordinate="
        + widthCoordinate
        + ", length="
        + length
        + ", lengthCoordinate="
        + lengthCoordinate
        + '}';
  }
}
