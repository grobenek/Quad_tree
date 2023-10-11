package entity;

import entity.shape.GpsCoordinates;

import java.util.List;

public class Property {
  private int registerNumber;
  private String description;
  private List<Parcel> parcels;
  private GpsCoordinates[] gpsCoordinates;
}
