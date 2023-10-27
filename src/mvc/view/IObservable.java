package mvc.view;

import java.util.List;
import quadtree.IShapeData;
import quadtree.QuadTree;

public interface IObservable {
  void attach(IObserver observer);

  void detach(IObserver observer);

  void sendNotifications();

  QuadTree<? extends IShapeData>[] getTrees();
}
