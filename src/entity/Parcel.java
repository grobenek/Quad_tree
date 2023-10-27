package entity;

import entity.shape.Rectangle;
import quadtree.IShapeData;

import java.util.List;

public class Parcel implements IShapeData {
  private int parcelNumber;
  private String description;
  private List<Property> properties;
  private Rectangle shape;

  public Parcel(int parcelNumber, String description, List<Property> properties, Rectangle shape) {
    this.parcelNumber = parcelNumber;
    this.description = description;
    this.properties = properties;
    this.shape = shape;
  }

  public Parcel(int parcelNumber, String description, Rectangle shape) {
    this.parcelNumber = parcelNumber;
    this.description = description;
    this.shape = shape;
  }

  public int getParcelNumber() {
    return parcelNumber;
  }

  public String getDescription() {
    return description;
  }

  public List<Property> getProperties() {
    return properties;
  }

  @Override
  public Rectangle getShapeOfData() {
    return shape;
  }

  @Override
  public String toString() {
    return "Parcel{" +
            "parcelNumber=" + parcelNumber +
            ", description='" + description + '\'' +
            ", properties=" + properties +
            ", shape=" + shape +
            '}';
  }

   @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Parcel)) {
      return false;
    }
    Parcel castedObj = (Parcel) obj;

    return (castedObj.description.equals(description)
            && ((castedObj.properties == null && properties == null)
                || (castedObj.properties != null && castedObj.properties.equals(properties)))
            && castedObj.parcelNumber == parcelNumber
            && (castedObj.shape == null && shape == null)
        || (castedObj.shape != null && castedObj.shape.equals(shape)));
  }
}
