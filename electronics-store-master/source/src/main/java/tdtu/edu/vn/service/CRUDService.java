package tdtu.edu.vn.service;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@SuppressWarnings("unchecked")
@AllArgsConstructor
public class CRUDService<T, ID, R extends JpaRepository<T, ID>> {
    protected R repository;

    public T save(T entity) {
        return repository.save(entity);
    }

    public T findById(ID id) {
        return repository.findById(id).orElse(null);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T update(T entity) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (repository.existsById((ID) entity.getClass().getMethod("getId").invoke(entity))) {
            return repository.save(entity);
        }
        return null;
    }

    public void delete(T entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ID id = (ID) entity.getClass().getMethod("getId").invoke(entity);
        if(repository.existsById(id))
            repository.deleteById(id);
    }
}
