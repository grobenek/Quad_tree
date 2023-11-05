package util.file;

import entity.SpatialData;
import java.io.IOException;
import java.util.List;

public class IOManager {
  IFileBuilder fileBuilder;

  public IOManager(IFileBuilder fileBuilder) {
    this.fileBuilder = fileBuilder;
  }

  public void saveToFile(String pathToFile, List<? extends SpatialData<?>> itemsToSave)
      throws IOException {
    fileBuilder.saveToFile(pathToFile, itemsToSave);
  }

  public void loadFromFile(String pathToFile) throws IOException {
    fileBuilder.loadFromFile(pathToFile);
  }
}
