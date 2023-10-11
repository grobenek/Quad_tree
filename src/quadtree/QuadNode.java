package quadtree;

import entity.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuadNode<T> {
  public static final int MAX_CHILDREN = 4;
  private final QuadNode<T>[] children;
  private final Rectangle shape;
  private final List<T> items;

  public QuadNode(QuadNode<T>[] children, Rectangle shape, List<T> items) {
    this.children = children;
    this.shape = shape;
    this.items = items;
  }

  public QuadNode(QuadNode<T>[] children, Rectangle shape) {
    this.children = children;
    this.shape = shape;
    this.items = new ArrayList<>();
  }

  public QuadNode(Rectangle shape) {
    this.children = (QuadNode<T>[]) new QuadNode[MAX_CHILDREN]; // TODO warning
    this.shape = shape;
    this.items = new ArrayList<>();
  }

  public T getItem(int index) {
    return items.get(index);
  }

  public T getItem(T item) {
    int indexOfFirstItem = items.indexOf(item);

    return indexOfFirstItem != -1 ? items.get(indexOfFirstItem) : null;
  }

  public void addItem(T item) {
    items.add(item);
  }

  public int getItemsSize() {
    return items.size();
  }

  public List<T> getAllItems() {
    return items;
  }

  public Rectangle getShape() {
    return shape;
  }

  public QuadNode<T>[] getChildren() {
    return children;
  }

  public QuadNode<T> getChild(int index) {
    if (index < 0 || index > MAX_CHILDREN - 1) {
      return null;
    }

    return children[index];
  }

  public QuadNode<T> getChild(Direction direction) {
    if (direction == null) {
      return null;
    }

    return children[direction.ordinal()];
  }

  public void addChild(QuadNode<T> child, Direction direction) {
    if (children[direction.ordinal()] != null) {
      throw new IllegalStateException(
          String.format("Child at direction %s already exists!", direction.name()));
    }

    children[direction.ordinal()] = child;
  }

  public void addChild(QuadNode<T> child, int index) {
    if (index < 0 || index > MAX_CHILDREN - 1) {
      throw new IndexOutOfBoundsException(String.format("Index %d is out of bounds!", index));
    }

    if (children[index] != null) {
      throw new IllegalStateException(String.format("Child at index %d already exists!", index));
    }

    children[index] = child;
  }

  public int getChildrenSize() {
    return children.length;
  }

  @Override
  public String toString() {
    return "QuadNode{"
        + "children="
        + Arrays.toString(children)
        + ", gpsCoordinates="
        + shape
        + ", items="
        + items
        + '}';
  }
}
