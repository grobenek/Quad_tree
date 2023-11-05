package mvc.controller;

import entity.Parcel;
import entity.Property;
import entity.shape.Rectangle;
import java.io.IOException;
import java.util.List;
import mvc.model.IModel;
import mvc.view.*;
import mvc.view.constant.DataType;
import mvc.view.observable.IObservable;
import mvc.view.observable.IObserver;
import mvc.view.observable.IQuadTreeObservable;
import quadtree.IShapeData;
import quadtree.QuadTree;
import util.file.IFileBuilder;

public class Controller implements IController, IObserver {
  IModel model;
  IMainWindow view;

  public Controller(IModel model) {
    this.model = model;
    model.attach(this);
  }

  @Override
  public List<Parcel> searchParcelsInGivenShape(Rectangle shapeToSearchIn) {
    try {
      return model.searchParcelsInGivenShape(shapeToSearchIn);
    } catch (Exception exception) {
      view.showPopupMessage(exception.getLocalizedMessage());
      return null;
    }
  }

  @Override
  public List<Property> searchPropertiesInGivenShape(Rectangle shapeToSearchIn) {
    try {
      return model.searchPropertiesInGivenShape(shapeToSearchIn);
    } catch (Exception exception) {
      view.showPopupMessage(exception.getLocalizedMessage());
      return null;
    }
  }

  @Override
  public List<? extends IShapeData>[] searchAllObjectsInGivenShape(Rectangle shapeToSearchIn) {
    try {
      return model.searchAllObjectsInGivenShape(shapeToSearchIn);
    } catch (Exception exception) {
      view.showPopupMessage(exception.getLocalizedMessage());
      return null;
    }
  }

  @Override
  public void addProperty(int registerNumber, String description, Rectangle shape) {
    try {
      model.addProperty(registerNumber, description, shape, true);
    } catch (Exception exception) {
      view.showPopupMessage(exception.getLocalizedMessage());
    }
  }

  @Override
  public void addParcel(int parcelNumber, String description, Rectangle shape) {
    try {
      model.addParcel(parcelNumber, description, shape, true);
    } catch (Exception exception) {
      view.showPopupMessage(exception.getLocalizedMessage());
    }
  }

  @Override
  public void deleteProperty(Property propertyToDelete) {
    model.deleteProperty(propertyToDelete);
  }

  @Override
  public void deleteParcel(Parcel parcelToDelete) {
    model.deleteParcel(parcelToDelete);
  }

  @Override
  public void initializePropertyQuadTree(int height, Rectangle shape) {
    try {
      model.initializePropertyQuadTree(height, shape);
    } catch (Exception exception) {
      view.showPopupMessage(exception.getLocalizedMessage());
    }
  }

  @Override
  public void initializeParcelQuadTree(int height, Rectangle shape) {
    try {
      model.initializeParcelQuadTree(height, shape);
    } catch (Exception exception) {
      view.showPopupMessage(exception.getLocalizedMessage());
    }
  }

  @Override
  public void generateData(int numberOfProperties, int numberOfParcels) {
    model.generateData(numberOfProperties, numberOfParcels);
  }

  @Override
  public void saveDataFromFile(String pathToFile, DataType dataType, IFileBuilder fileBuilder)
      throws IOException {
    model.saveDataFromFile(pathToFile, dataType, fileBuilder);
  }

  @Override
  public void loadDataFromFile(String pathToFile, IFileBuilder fileBuilder) throws IOException {
    model.loadDataFromFile(pathToFile, fileBuilder);
  }

  @Override
  public void optimizeTrees() {
    model.optimizeTrees();
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
