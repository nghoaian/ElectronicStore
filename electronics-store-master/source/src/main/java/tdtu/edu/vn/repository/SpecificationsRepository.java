package tdtu.edu.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tdtu.edu.vn.model.specifications.Specifications;

@Repository
public interface SpecificationsRepository extends JpaRepository<Specifications, Integer> {
    @Query("delete from Specifications s where s.product.id = ?1")
    void deleteByProductId(int productId);
}
