package entity;

import entity.shape.Rectangle;
import java.util.List;
import quadtree.IShapeData;

public class Property implements IShapeData {
  private int registerNumber;
  private String description;
  private List<Parcel> parcels;
  private Rectangle shape;

  public Property(int registerNumber, String description, Rectangle shape) {
    this.registerNumber = registerNumber;
    this.description = description;
    this.shape = shape;
  }

  public Property(int registerNumber, String description) {
    this.registerNumber = registerNumber;
    this.description = description;
  }

  @Override
  public Rectangle getShapeOfData() {
    return shape;
  }

  @Override
  public String toString() {
    return "Property{"
        + "registerNumber="
        + registerNumber
        + ", description='"
        + description
        + '\''
        + ", parcels="
        + parcels
        + ", shape="
        + shape
        + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Property)) {
      return false;
    }
    Property castedObj = (Property) obj;

    return (castedObj.description.equals(description)
        && ((castedObj.parcels == null && parcels == null) || castedObj.parcels.equals(parcels))
        && castedObj.registerNumber == registerNumber
        && castedObj.shape.equals(shape));
  }
}
