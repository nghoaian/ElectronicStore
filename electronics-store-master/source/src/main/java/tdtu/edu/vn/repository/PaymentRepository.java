package tdtu.edu.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tdtu.edu.vn.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
