package tdtu.edu.vn.service;

import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.Evaluation;
import tdtu.edu.vn.repository.EvaluationRepository;

@Service
public class EvaluationService extends CRUDService<Evaluation, Integer, EvaluationRepository> {
    public EvaluationService(EvaluationRepository repository) {
        super(repository);
    }

    public void deleteByProductId(Integer productId) {
        repository.deleteByProductId(productId);
    }
}
