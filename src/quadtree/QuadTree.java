package quadtree;

import entity.shape.Rectangle;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class QuadTree<T extends IShapeData> {
  private final int MAX_HEIGHT;

  private Rectangle shape;
  private QuadNode<T> root;

  public QuadTree(int maxHeight, Rectangle shape) {
    this.MAX_HEIGHT = maxHeight;
    this.shape = shape;
    this.root = new QuadNode<>(shape);
  }

  public Rectangle getShape() {
    return shape;
  }

  public int getMAX_HEIGHT() {
    return MAX_HEIGHT;
  }

  public QuadNode<T> insert(T data) {
    Rectangle shape = data.getShapeOfData();
    if (shape == null) {
      throw new IllegalArgumentException("Shape of inserted data is null!");
    }

    if (!checkIfDataFitIntoRoot(data)) {
      throw new IllegalArgumentException(
          String.format(
              "Data: %s does not fit into QuadTree with shape: %s", data, root.getShape()));
    }

    if (root == null) {
      root = new QuadNode<>(data, shape);
      this.shape = shape;
      return root;
    }

    // finding right place for data
    return findFreeParentForDataAndAddDataToNewChild(root, data);
  }

  private boolean isPoint(T data) {
    return (data.getShapeOfData().getWidth() == data.getShapeOfData().getLength());
  }

  private void insertPolygon(T data) {}

  /**
   * Finds suitable place for data and returns node where data was inserted
   *
   * @param startingPoint Node from which we look for suitable place
   * @param data data to be added
   * @return Node where data was added
   */
  private QuadNode<T> findFreeParentForDataAndAddDataToNewChild(QuadNode<T> startingPoint, T data) {
    if (startingPoint == null) {
      throw new IllegalArgumentException(
          "Cannot find free parent for data when startingPoint is null!");
    }

    QuadNode<T> parentOfPlace = startingPoint;
    Quadrant quadrantForDataToBePlaced = startingPoint.getQuadrantOfShape(data);

    // quadrant is null - add data to parent and end
    if (quadrantForDataToBePlaced == null) {
      startingPoint.addItem(data);
      return startingPoint;
    }

    QuadNode<T> possiblePlaceForData = parentOfPlace.getChild(quadrantForDataToBePlaced);

    // free place for data - generate child and add data and return
    if (possiblePlaceForData == null) {
      return tryToInsertOldDataIntoNewChildrenAndInsertNewData(parentOfPlace, data);
    }

    // finding free place for items or until max height is reached
    int heightCounter = possiblePlaceForData.getHeight();
    while (possiblePlaceForData != null) {
      if (heightCounter == MAX_HEIGHT) {
        possiblePlaceForData.addItem(data);
        return possiblePlaceForData;
      }

      parentOfPlace = possiblePlaceForData;
      quadrantForDataToBePlaced = parentOfPlace.getQuadrantOfShape(data);

      // quadrant is null - add data to parent and end
      if (quadrantForDataToBePlaced == null) {
        parentOfPlace.addItem(data);
        return parentOfPlace;
      }

      possiblePlaceForData = parentOfPlace.getChild(quadrantForDataToBePlaced);
      heightCounter++;
    }

    // free space for data
    return tryToInsertOldDataIntoNewChildrenAndInsertNewData(parentOfPlace, data);
  }

  private QuadNode<T> tryToInsertOldDataIntoNewChildrenAndInsertNewData(
      QuadNode<T> originalParent, T data) {
    Stack<T> stackOfItemsThatNeedSToBeReinserted = new Stack<>();

    // insert data - will process as last
    stackOfItemsThatNeedSToBeReinserted.push(data);
    stackOfItemsThatNeedSToBeReinserted.addAll(originalParent.getItems());
    originalParent.removeAllItems();

    while (!stackOfItemsThatNeedSToBeReinserted.isEmpty()) {
      T itemToWorkWith = stackOfItemsThatNeedSToBeReinserted.pop();

      QuadNode<T> parentOfPlace = originalParent;
      int heightCounter = parentOfPlace.getHeight();
      Quadrant quadrantOfItem = parentOfPlace.getQuadrantOfShape(itemToWorkWith);

      // item cannot be inserted deeper, adding to parent
      if (quadrantOfItem == null) {
        parentOfPlace.addItem(itemToWorkWith);
        continue;
      }

      QuadNode<T> possiblePlaceForItem = parentOfPlace.getChild(quadrantOfItem);

      // item can be inserted
      if (possiblePlaceForItem == null) {
        parentOfPlace.generateChild(quadrantOfItem).addItem(itemToWorkWith);
        continue;
      }
      heightCounter++;

      // finding possible place for item
      while (true) {
        parentOfPlace = possiblePlaceForItem;
        quadrantOfItem = parentOfPlace.getQuadrantOfShape(itemToWorkWith);

        // item cannot be inserted deeper, adding to parent
        if (quadrantOfItem == null) {
          parentOfPlace.addItem(itemToWorkWith);
          break;
        }

        possiblePlaceForItem = parentOfPlace.getChild(quadrantOfItem);

        // item can be inserted
        if (possiblePlaceForItem == null) {
          if (parentOfPlace.getItemsSize() != 0) {
            stackOfItemsThatNeedSToBeReinserted.addAll(parentOfPlace.getItems());
            parentOfPlace.removeAllItems();
          }
          parentOfPlace.generateChild(quadrantOfItem).addItem(itemToWorkWith);
          break;
        }
        heightCounter++;
      }
    }

    return null;
  }

  /**
   * Finds and returns all data that are within given shape
   *
   * @param shape shape where to search for data
   * @return LinkedList with result data
   */
  public List<T> search(Rectangle shape) {
    if (shape == null) {
      throw new IllegalArgumentException("Cannot find any data for null shape!");
    }

    if (!root.doesOverlapWithRectangle(shape)) {
      throw new IllegalArgumentException(
          String.format(
              "Shape %s does not overlap with quad tree with root %s", shape, root.getShape()));
    }

    List<T> result = new LinkedList<>();
    Stack<QuadNode<T>> nodesToProcess = new Stack<>();
    QuadNode<T> currentNode = root;

    nodesToProcess.addAll(
        Arrays.stream(currentNode.getChildren())
            .filter(child -> child != null && child.doesOverlapWithRectangle(shape))
            .toList());

    currentNode.getItems().stream()
        .filter(item -> item.getShapeOfData().doesOverlapWithRectangle(shape))
        .forEach(result::add);

    while (!nodesToProcess.empty()) {
      currentNode = nodesToProcess.pop();
      // add children for proccesing when they are overlapping with shape
      nodesToProcess.addAll(
          Arrays.stream(currentNode.getChildren())
              .filter(child -> child != null && child.doesOverlapWithRectangle(shape))
              .toList());

      if (currentNode.getItemsSize() == 0) {
        continue;
      }

      // add item within shape to result
      currentNode.getItems().stream()
          .filter(item -> item.getShapeOfData().doesOverlapWithRectangle(shape))
          .forEach(result::add);
    }

    return result;
  }

  private boolean checkIfDataFitIntoRoot(T data) {
    Rectangle shapeOfData = data.getShapeOfData();
    return ((shapeOfData.getFirstPoint().widthCoordinate()
                > root.getShape().getFirstPoint().widthCoordinate()
            && shapeOfData.getFirstPoint().lengthCoordinate()
                > root.getShape().getFirstPoint().lengthCoordinate()
            && shapeOfData.getFirstPoint().widthCoordinate()
                < root.getShape().getSecondPoint().widthCoordinate()
            && shapeOfData.getFirstPoint().lengthCoordinate()
                < root.getShape().getSecondPoint().lengthCoordinate())
        && (shapeOfData.getSecondPoint().widthCoordinate()
                < root.getShape().getSecondPoint().widthCoordinate()
            && shapeOfData.getSecondPoint().lengthCoordinate()
                < root.getShape().getSecondPoint().lengthCoordinate()
            && shapeOfData.getSecondPoint().widthCoordinate()
                > root.getShape().getFirstPoint().widthCoordinate()
            && shapeOfData.getSecondPoint().lengthCoordinate()
                > root.getShape().getFirstPoint().lengthCoordinate()));
  }
}
