package mvc.view.observable;

import quadtree.IShapeData;
import quadtree.QuadTree;

public interface IQuadTreeObservable extends IObservable {
    QuadTree<? extends IShapeData>[] getTrees();
}
