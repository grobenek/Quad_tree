package entity;

import entity.shape.Rectangle;

import java.util.LinkedList;
import java.util.List;
import quadtree.IShapeData;

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
    this.properties = new LinkedList<>();
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
    return "Parcel{"
        + "parcelNumber="
        + parcelNumber
        + ", description='"
        + description
        + '\''
        + ", properties="
        + properties.size()
        + ", shape="
        + shape
        + '}';
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

  public void setProperties(List<Property> properties) {
    this.properties = properties;
  }

  public void setParcelNumber(int parcelNumber) {
    this.parcelNumber = parcelNumber;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void addProperty(Property property) {
    properties.add(property);
  }

  public void removeProperty(Property propertyToDelete) {
    properties.remove(propertyToDelete);
  }
}
