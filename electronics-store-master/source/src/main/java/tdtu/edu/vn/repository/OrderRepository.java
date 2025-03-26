package tdtu.edu.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.model.Order.Status;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.user.id = ?1 AND o.status = ?2")
    Order findByUserIdAndOrderStatus(int userId, Status status);
}
