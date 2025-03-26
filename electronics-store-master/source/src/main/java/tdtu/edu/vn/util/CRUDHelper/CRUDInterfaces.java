package tdtu.edu.vn.util.CRUDHelper;

import org.springframework.http.ResponseEntity;

public interface CRUDInterfaces<T> {
    ResponseEntity<?> create(T entity);
    ResponseEntity<?> update(T entity);
    ResponseEntity<?> delete(T entity);
    ResponseEntity<?> show(T entity);
    ResponseEntity<?> search(T entity);
}
