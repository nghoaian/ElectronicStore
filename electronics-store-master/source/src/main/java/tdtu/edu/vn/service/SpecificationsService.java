package tdtu.edu.vn.service;

import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.specifications.Specifications;
import tdtu.edu.vn.repository.SpecificationsRepository;

@Service
public class SpecificationsService extends CRUDService<Specifications, Integer, SpecificationsRepository>{
    public SpecificationsService(SpecificationsRepository repository) {
        super(repository);
    }

    public void deleteByProductId(int id){
        this.repository.deleteByProductId(id);
    }
}
