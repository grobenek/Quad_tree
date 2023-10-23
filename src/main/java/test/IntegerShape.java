package test;

import entity.shape.Rectangle;
import quadtree.IShapeData;

public class IntegerShape implements IShapeData {
  private final int data;
  private final Rectangle shape;

  public IntegerShape(int data, Rectangle shape) {
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
