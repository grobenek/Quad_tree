package mvc.controller;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;

import java.util.Arrays;
import java.util.List;
import mvc.model.IModel;
import mvc.view.IObservable;
import mvc.view.IObserver;
import mvc.view.MainWindow;
import quadtree.IShapeData;
import quadtree.QuadTree;

public class Controller implements IController, IObserver {
  IModel model;
  MainWindow view;

  public Controller(IModel model, MainWindow mainWindow) {
    this.model = model;
    this.view = mainWindow;

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
  public void update(IObservable observable) {
    QuadTree<? extends IShapeData>[] trees = observable.getTrees();

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
}
