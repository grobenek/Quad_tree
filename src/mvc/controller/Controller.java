package mvc.controller;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import java.util.List;
import mvc.model.IModel;
import mvc.view.*;
import mvc.view.observable.IObservable;
import mvc.view.observable.IObserver;
import mvc.view.observable.IQuadTreeObservable;
import quadtree.IShapeData;
import quadtree.QuadTree;

public class Controller implements IController, IObserver {
  IModel model;
  IMainWindow view;

  public Controller(IModel model) {
    this.model = model;
    model.attach(this);
  }

  @Override
  public List<Parcel> searchParcelsInGivenShape(Rectangle shapeToSearchIn) {
    return model.searchParcelsInGivenShape(shapeToSearchIn);
  }

  @Override
  public List<Property> searchPropertiesInGivenShape(Rectangle shapeToSearchIn) {
    return model.searchPropertiesInGivenShape(shapeToSearchIn);
  }

  @Override
  public List<? extends IShapeData>[] searchAllObjectsInGivenShape(Rectangle shapeToSearchIn) {
    return model.searchAllObjectsInGivenShape(shapeToSearchIn);
  }

  @Override
  public Property addProperty(int registerNumber, String description, Rectangle shape) {
    return model.addProperty(registerNumber, description, shape);
  }

  @Override
  public void initializePropertyQuadTree(int height, Rectangle shape) {
    model.initializePropertyQuadTree(height, shape);
  }

  @Override
  public void initializeParcelQuadTree(int height, Rectangle shape) {
    model.initializeParcelQuadTree(height, shape);
  }

  @Override
  public void update(IObservable observable) {
    if (!(observable instanceof IQuadTreeObservable)) {
      return;
    }

    QuadTree<? extends IShapeData>[] trees = ((IQuadTreeObservable) observable).getTrees();

    if (trees.length == 0) {
      return;
    }

    for (int i = 0; i < trees.length; i++) {
      if (i == 0 && trees[i] != null) {
        view.setParcelTreeInfo((QuadTree<Parcel>) trees[i]);
      }

      if (i == 1 && trees[i] != null) {
        view.setPropertyTreeInfo((QuadTree<Property>) trees[i]);
      }
    }
  }

  public void setView(IMainWindow view) {
    this.view = view;
    view.initializeBothQuadTrees();
  }
}
