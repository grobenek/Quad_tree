package quadtree;

import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import java.util.*;

public class QuadTree<T extends IShapeData> {
  private final int MAX_HEIGHT;

  private Rectangle shape;
  private QuadNode<T> root;
  private int size;

  public QuadTree(int maxHeight, Rectangle shape) {
    this.MAX_HEIGHT = maxHeight;
    this.shape = shape;
    this.root = new QuadNode<>(shape);
    this.size = 0;
  }

  public Rectangle getShape() {
    return shape;
  }

  public int getMAX_HEIGHT() {
    return MAX_HEIGHT;
  }

  public void insert(T data) {
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
      return;
    }

    // finding right place for data
    findFreeParentForDataAndAddDataToNewChild(root, data, true);
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
   */
  private QuadNode<T> findFreeParentForDataAndAddDataToNewChild(
      QuadNode<T> startingPoint, T data, boolean insertNewData) {
    if (startingPoint == null) {
      throw new IllegalArgumentException(
          "Cannot find free parent for data when startingPoint is null!");
    }

    QuadNode<T> parentOfPlace = startingPoint;
    Quadrant quadrantForDataToBePlaced = startingPoint.getQuadrantOfShape(data, false);

    // quadrant is null - add data to parent and end
    if (quadrantForDataToBePlaced == null) {
      if (insertNewData) {
        startingPoint.addItem(data);
        size++;
        return null;
      }
      return startingPoint;
    }

    QuadNode<T> possiblePlaceForData = parentOfPlace.getChild(quadrantForDataToBePlaced);

    // free place for data - generate child and add data and return
    if (possiblePlaceForData == null) {
      if (insertNewData) {
        tryToInsertOldDataIntoNewChildrenAndInsertNewData(parentOfPlace, data);
        size++;
        return null;
      }
      return parentOfPlace;
    }

    // finding free place for items or until max height is reached
    int heightCounter = possiblePlaceForData.getHeight();
    while (possiblePlaceForData != null) {
      if (heightCounter == MAX_HEIGHT) {
        if (insertNewData) {
          possiblePlaceForData.addItem(data);
          size++;
          return null;
        }
        return possiblePlaceForData;
      }

      parentOfPlace = possiblePlaceForData;
      quadrantForDataToBePlaced = parentOfPlace.getQuadrantOfShape(data, false);

      // quadrant is null - add data to parent and end
      if (quadrantForDataToBePlaced == null) {
        if (insertNewData) {
          parentOfPlace.addItem(data);
          size++;
          return null;
        }
        return parentOfPlace;
      }

      possiblePlaceForData = parentOfPlace.getChild(quadrantForDataToBePlaced);
      heightCounter++;
    }
    // free space for data
    if (insertNewData) {
      tryToInsertOldDataIntoNewChildrenAndInsertNewData(parentOfPlace, data);
      size++;
      return null;
    }
    return parentOfPlace;
  }

  private void tryToInsertOldDataIntoNewChildrenAndInsertNewData(
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
      Quadrant quadrantOfItem = parentOfPlace.getQuadrantOfShape(itemToWorkWith, false);

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
        quadrantOfItem = parentOfPlace.getQuadrantOfShape(itemToWorkWith, false);

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
  }

  /**
   * Finds and returns all data that are within given shape
   *
   * @param shape shape where to search for data
   * @return List with result data
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

    Stack<QuadNode<T>> nodesToProcess = new Stack<>();
    QuadNode<T> currentNode = root;

    nodesToProcess.addAll(
        Arrays.stream(currentNode.getChildren())
            .filter(child -> child != null && child.doesOverlapWithRectangle(shape))
            .toList());

    List<T> result = // creating result list with first filtered items from root
        new LinkedList<>(
            currentNode.getItems().stream()
                .filter(item -> item.getShapeOfData().doesOverlapWithRectangle(shape))
                .toList());

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

  public void deleteData(T data) {
    if (data == null) {
      throw new IllegalArgumentException("Cannot delete data: null!");
    }

    QuadNode<T> nodeOfChild = findFreeParentForDataAndAddDataToNewChild(root, data, false);

    if (nodeOfChild == null) {
      throw new IllegalStateException(String.format("Data %s not found in quadtree", data));
    }

    T foundData = nodeOfChild.getItem(data);

    if (foundData == null) {
      throw new IllegalStateException(
          String.format("Data %s not found in area %s", data, nodeOfChild.getShape()));
    }

    nodeOfChild.removeItem(foundData);
    size--;

    // is leaf, but it is not empty
    if (nodeOfChild.isLeaf() && nodeOfChild.getItemsSize() != 0) {
      return;
    }

    // leaf is empty
    // remove child from its parent
    if (nodeOfChild.isLeaf() && nodeOfChild.getItemsSize() == 0) {
      QuadNode<T> parentOfChild = nodeOfChild.getParent();

      // parent is null - current node is root
      if (parentOfChild == null) {
        return;
      }

      Quadrant quadrantOfChild = parentOfChild.getQuadrantOfShape(nodeOfChild.getShape(), true);

      if (quadrantOfChild == null) {
        throw new IllegalStateException(
            String.format(
                "Quadrant for child: %s not found in parent %s", nodeOfChild, parentOfChild));
      }

      parentOfChild.removeChild(quadrantOfChild);

      // has another child, cannot remove him
      if (!parentOfChild.isLeaf()) {
        return;
      }

      // parent has 0 children
      // deleting empty parents
      while (true) {
        nodeOfChild = parentOfChild;
        parentOfChild = nodeOfChild.getParent();

        if (!parentOfChild.isLeaf()) {
          return;
        }

        if (nodeOfChild.getItemsSize() != 0) {
          return;
        }

        quadrantOfChild = parentOfChild.getQuadrantOfShape(nodeOfChild.getShape(), true);

        if (quadrantOfChild == null) {
          throw new IllegalStateException(
              String.format(
                  "Quadrant for child: %s not found in parent %s", nodeOfChild, parentOfChild));
        }

        parentOfChild.removeChild(quadrantOfChild);
      }
    }
  }

  public int getSize() {
    return size;
  }

  private boolean checkIfDataFitIntoRoot(T data) {
    GpsCoordinates firstPointOfData = data.getShapeOfData().getFirstPoint();
    GpsCoordinates secondPointOfData = data.getShapeOfData().getSecondPoint();

    GpsCoordinates thisFirstPoint = shape.getFirstPoint();
    GpsCoordinates thisSecondPoint = shape.getSecondPoint();

    return (firstPointOfData.widthCoordinate() >= thisFirstPoint.widthCoordinate()
        && secondPointOfData.widthCoordinate() <= thisSecondPoint.widthCoordinate()
        && firstPointOfData.lengthCoordinate() >= thisFirstPoint.lengthCoordinate()
        && secondPointOfData.lengthCoordinate() <= thisSecondPoint.lengthCoordinate());
  }
}
