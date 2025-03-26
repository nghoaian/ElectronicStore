package tdtu.edu.vn.util.CRUDDecoratorHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.User;
import tdtu.edu.vn.util.CRUDHelper.CRUDInterfaces;

@Service
public abstract class CRUDAuthorization<T> extends CRUDDecorator<T>{
    @Autowired(required = false)
    public CRUDAuthorization(CRUDInterfaces<T> wrapObjectCrud) {
        super(wrapObjectCrud);
    }

    @Override
    public ResponseEntity<?> create(T entity) {
        User user = getUserFromRequest(entity);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return wrapObjectCrud.create(entity);
    }

    @Override
    public ResponseEntity<?> update(T entity) {
        User user = getUserFromRequest(entity);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return wrapObjectCrud.update(entity);
    }

    @Override
    public ResponseEntity<?> delete(T entity) {
        User user = getUserFromRequest(entity);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return wrapObjectCrud.delete(entity);
    }

    @Override
    public ResponseEntity<?> show(T entity) {
        User user = getUserFromRequest(entity);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return wrapObjectCrud.show(entity);
    }

    @Override
    public ResponseEntity<?> search(T entity) {
        User user = getUserFromRequest(entity);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return wrapObjectCrud.search(entity);
    }
    
    abstract User getUserFromRequest(T entity);
}
