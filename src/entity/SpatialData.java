package entity;

import entity.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;
import quadtree.IShapeData;

public abstract class SpatialData<T extends IShapeData> implements IShapeData {
  private int identificationNumber;
  private String description;
  private List<T> relatedDataList;
  private Rectangle shape;

  public SpatialData(int identificationNumber, String description, Rectangle shape) {
    this.identificationNumber = identificationNumber;
    this.description = description;
    this.shape = shape;

    this.relatedDataList = new ArrayList<>();
  }

  public SpatialData(int identificationNumber, String description) {
    this.identificationNumber = identificationNumber;
    this.description = description;

    this.relatedDataList = new ArrayList<>();
  }

  public int getIdentificationNumber() {
    return identificationNumber;
  }

  public void setIdentificationNumber(int identificationNumber) {
    this.identificationNumber = identificationNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<T> getRelatedDataList() {
    return relatedDataList;
  }

  public void setRelatedDataList(List<T> relatedDataList) {
    this.relatedDataList = relatedDataList;
  }

  public void addRelatedData(T data) {
    relatedDataList.add(data);
  }

  public void removeRelatedData(T data) {
    relatedDataList.remove(data);
  }

  public void setShape(Rectangle shape) {
    this.shape = shape;
  }

  @Override
  public Rectangle getShapeOfData() {
    return shape;
  }

  public String toString(String className) {
    return className
        + "{"
        + "identificationNumber="
        + identificationNumber
        + ", description='"
        + description
        + '\''
        + ", relatedDataList="
        + relatedDataList.size()
        + ", shape="
        + shape
        + '}'
        + "\n";
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof SpatialData<?>)) {
      return false;
    }
    SpatialData<?> castedObj = (SpatialData<?>) obj;

    return (castedObj.getDescription().equals(description)
            && ((castedObj.relatedDataList == null && relatedDataList == null)
                || (castedObj.relatedDataList != null
                    && castedObj.relatedDataList.equals(relatedDataList)))
            && castedObj.identificationNumber == identificationNumber
            && (castedObj.shape == null && shape == null)
        || (castedObj.shape != null && castedObj.shape.equals(shape)));
  }
}
