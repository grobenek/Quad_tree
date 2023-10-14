package entity;

import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import quadtree.IShapeData;

import java.util.List;

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
}
