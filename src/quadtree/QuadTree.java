package quadtree;

import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;

public class QuadTree<T> {
  public static final int MAX_CHILDREN = 4;
  private QuadNode<T>[] children;
  private final int height;
  private QuadNode<T> root;

  public QuadTree(int maxHeight, Rectangle shape) {
    this.height = maxHeight;
    this.root = new QuadNode<>(shape);
  }

  public int getHeight() {
    return height;
  }

  public void insert(T data, Rectangle shape) {
    if (isPoint(shape)) {
      insertPoint(data, shape);
    } else {
      insertPolygon(data, shape);
    }
  }

  private boolean isPoint(Rectangle shape) {
    return (shape.getWidth() == shape.getLength());
  }

  private void insertPolygon(T data, Rectangle rectangle) {}

  private void insertPoint(T data, Rectangle rectangle) {}
}
