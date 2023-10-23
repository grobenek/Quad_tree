package entity;

import entity.shape.GpsCoordinates;

import java.util.List;

public class Parcel {
  private int parcelNumber;
  private String description;
  private List<Property> properties;
  private GpsCoordinates[] gpsCoordinates;
}
