package util.file;

import entity.SpatialData;
import entity.shape.Direction;
import entity.shape.GpsCoordinates;
import entity.shape.Rectangle;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class CsvBuilder implements IFileBuilder {
  private final List<SpatialData<?>> loadedItems;
  private static final char deliminer = ';';

  public CsvBuilder() {
    this.loadedItems = new LinkedList<>();
  }

  @Override
  public void saveToFile(String pathToFile, List<? extends SpatialData<?>> itemsToSave)
      throws IOException {

    if (itemsToSave.isEmpty()) {
      return;
    }

    StringBuilder sb = new StringBuilder();

    sb.append("Object name;Description;X1;Y1;X2;Y2\n");
    sb.append(itemsToSave.get(0).getClass().getName());
    sb.append("\n");

    for (SpatialData<?> spatialData : itemsToSave) {
      sb.append(spatialData.getIdentificationNumber());
      sb.append(deliminer);
      sb.append(spatialData.getDescription());
      sb.append(deliminer);
      sb.append(spatialData.getShapeOfData().getFirstPoint().widthCoordinate());
      sb.append(deliminer);
      sb.append(spatialData.getShapeOfData().getFirstPoint().lengthCoordinate());
      sb.append(deliminer);
      sb.append(spatialData.getShapeOfData().getSecondPoint().widthCoordinate());
      sb.append(deliminer);
      sb.append(spatialData.getShapeOfData().getSecondPoint().lengthCoordinate());
      sb.append("\n");
    }

    try (DataOutputStream dataOutputStream =
        new DataOutputStream(new FileOutputStream(pathToFile))) {
      dataOutputStream.writeBytes(sb.toString());
    }
  }

  @Override
  public void loadFromFile(String pathToFile) throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile))) {
      bufferedReader.readLine(); // skip header line
      String className = bufferedReader.readLine();

      // getting class to create reading class name from file
      Class<?> classToCreate = Class.forName(className);

      // reading lines
      while (true) {
        String line = bufferedReader.readLine();

        if (line == null) {
          break;
        }

        // creating object using reflections
        String[] data = line.split(String.valueOf(deliminer));
        Rectangle shapeOfObject =
            new Rectangle(
                new GpsCoordinates(
                    Direction.S, Float.parseFloat(data[2]), Direction.W, Float.parseFloat(data[3])),
                new GpsCoordinates(
                    Direction.S,
                    Float.parseFloat(data[4]),
                    Direction.W,
                    Float.parseFloat(data[5])));
        Object createdObject =
            classToCreate.getConstructors()[0].newInstance(
                Integer.parseInt(data[0]), data[1], shapeOfObject);

        if (createdObject instanceof SpatialData) {
          loadedItems.add((SpatialData<?>) createdObject);
        }
      }
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Cannot load file. Cannot load class from file.");
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (InstantiationException | IllegalAccessException e) {
      throw new IllegalStateException(
          "Cannot load file: %s. Cannot create instance of class from file.");
    }
  }

  @Override
  public List<SpatialData<?>> getLoadedData() {
    return loadedItems;
  }
}
