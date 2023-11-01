package test;

import entity.SpatialData;
import entity.shape.Rectangle;
import quadtree.IShapeData;

public class IntegerShape extends SpatialData<IShapeData> implements IShapeData {
  private final int data;
  private final Rectangle shape;

  public IntegerShape(int data, Rectangle shape) {
    super(data, String.valueOf(data), shape);
    this.data = data;
    this.shape = shape;
  }

  public int getData() {
    return data;
  }

  @Override
  public Rectangle getShapeOfData() {
    return shape;
  }

  @Override
  public String toString() {
    return "IntegerShape{" + "data=" + data + '}';
  }
}
