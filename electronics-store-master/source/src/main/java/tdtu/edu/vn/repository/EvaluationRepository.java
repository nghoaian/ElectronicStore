package tdtu.edu.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tdtu.edu.vn.model.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    @Query("SELECT e FROM Evaluation e WHERE e.product.id = ?1")
    void deleteByProductId(Integer orderId);
}
