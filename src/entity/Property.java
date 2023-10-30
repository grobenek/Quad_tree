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

  public int getRegisterNumber() {
    return registerNumber;
  }

  public void setRegisterNumber(int registerNumber) {
    this.registerNumber = registerNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setShape(Rectangle shape) {
    this.shape = shape;
  }

  public void addParcel(Parcel parcel) {
    parcels.add(parcel);
  }

  public void removeParcel(Parcel parcel) {
    parcels.remove(parcel);
  }

  public void setParcels(List<Parcel> parcels) {
    this.parcels = parcels;
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
        + parcels.size()
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
            && ((castedObj.parcels == null && parcels == null)
                || (castedObj.parcels != null && castedObj.parcels.equals(parcels)))
            && castedObj.registerNumber == registerNumber
            && (castedObj.shape == null && shape == null)
        || (castedObj.shape != null && castedObj.shape.equals(shape)));
  }
}
