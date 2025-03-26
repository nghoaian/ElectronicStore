package tdtu.edu.vn.util.CRUDDecoratorHelper;

import lombok.AllArgsConstructor;
import tdtu.edu.vn.util.CRUDHelper.CRUDInterfaces;


@AllArgsConstructor
public abstract class CRUDDecorator<T> implements CRUDInterfaces<T> {
    protected CRUDInterfaces<T> wrapObjectCrud;
}
