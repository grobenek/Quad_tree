package entity;

import entity.shape.GpsCoordinates;

import java.util.List;

public class Property {
  private int registerNumber;
  private String description;
  private List<Parcel> parcels;
  private GpsCoordinates[] gpsCoordinates;

  public Property(int registerNumber, String description) {
    this.registerNumber = registerNumber;
    this.description = description;
  }
}
