package tdtu.edu.vn.util.CRUDHelper;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tdtu.edu.vn.payload.ResponseData;
import tdtu.edu.vn.service.CRUDService;

import java.lang.reflect.InvocationTargetException;

// strategy to define validation for CRUD operations
@AllArgsConstructor
public abstract class CRUD<T, R extends JpaRepository<T, Integer>> implements CRUDInterfaces<T>{
    protected final CRUDService<T, Integer, R> crudService;

    private Integer getId(T entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (Integer) entity.getClass().getMethod("getId").invoke(entity);
    }

    public ResponseEntity<?> delete(T entity){
        try{
            T getEntity = crudService.findById(getId(entity));
            if(getEntity == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entity.getClass().getName() + " not found");
            }
            crudService.delete(getEntity);
            return ResponseEntity.ok("Delete successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    public ResponseEntity<?> show(T entity){
        ResponseData.Builder responseData = new ResponseData.Builder();
        try{
            T getEntity = crudService.findById(getId(entity));
            if(getEntity == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(entity.getClass().getSimpleName() + " not found!!");
            }

            responseData.data(getEntity)
                    .message("Success to get " + entity.getClass().getSimpleName().toLowerCase() + " information");

            return ResponseEntity.ok(responseData.build());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
