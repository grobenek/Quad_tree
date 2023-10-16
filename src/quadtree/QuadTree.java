package quadtree;

import entity.shape.Rectangle;
import java.util.Stack;

public class QuadTree<T extends IShapeData> {
  private final int MAX_HEIGHT;
  private QuadNode<T> root;

  public QuadTree(int maxHeight, Rectangle shape) {
    this.MAX_HEIGHT = maxHeight;
    this.root = new QuadNode<>(shape);
  }

  public int getMAX_HEIGHT() {
    return MAX_HEIGHT;
  }

  public QuadNode<T> insert(T data) {
    Rectangle shape = data.getShapeOfData();
    if (shape == null) {
      throw new IllegalArgumentException("Shape of inserted data is null!");
    }

    if (root == null) {
      root = new QuadNode<>(data, shape);
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
}
