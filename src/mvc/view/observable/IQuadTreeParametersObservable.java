package mvc.view.observable;

import entity.shape.Rectangle;
import mvc.view.constant.DataType;

public interface IQuadTreeParametersObservable extends IObservable {
    int getQuadTreeHeight();
    Rectangle getQuadTreeArea();
    DataType getQuadTreeDataType();
}
