package entity.shape;

/**
 * @param width N or S
 * @param length E or W
 */
public record GpsCoordinates(
    char width, double widthCoordinate, char length, double lengthCoordinate) {

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
