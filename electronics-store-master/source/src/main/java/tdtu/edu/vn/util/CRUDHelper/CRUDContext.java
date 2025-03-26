package tdtu.edu.vn.util.CRUDHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CRUDContext {
    public CRUDInterfaces<?> crud;

    // crud factory
    @SuppressWarnings("unchecked")
    public <T> ResponseEntity<?> executeFactory(T entity, CRUDType type) {
        return switch (type) {
            case CREATE -> ((CRUDInterfaces<T>)crud).create(entity);
            case UPDATE -> ((CRUDInterfaces<T>)crud).update(entity);
            case DELETE -> ((CRUDInterfaces<T>)crud).delete(entity);
            case SHOW -> ((CRUDInterfaces<T>)crud).show(entity);
            case SEARCH -> ((CRUDInterfaces<T>)crud).search(entity);
        };
    }
}