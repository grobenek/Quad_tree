package util.file;

import entity.SpatialData;
import java.io.IOException;
import java.util.List;

public interface IFileBuilder {
  void saveToFile(String pathToFile, List<? extends SpatialData<?>> itemsToSave) throws IOException;

  void loadFromFile(String pathToFile) throws IOException;

  List<SpatialData<?>> getLoadedData();
}
