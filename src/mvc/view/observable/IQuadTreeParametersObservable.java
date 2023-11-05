package mvc.view.observable;

import entity.shape.Rectangle;

public interface IQuadTreeParametersObservable extends IObservable {
    int getQuadTreeHeight();
    Rectangle getQuadTreeArea();
}
