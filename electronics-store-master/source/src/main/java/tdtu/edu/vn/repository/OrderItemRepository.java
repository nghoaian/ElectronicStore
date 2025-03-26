package tdtu.edu.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tdtu.edu.vn.model.OrderItem;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    void deleteByOrderId(Integer orderId);

    void deleteByProductId(Integer productId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = ?1")
    List<OrderItem> findByOrderId(Integer orderId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = ?1 AND oi.product.id = ?2")
    OrderItem findByOrderIdAndProductId(Integer orderId, Integer productId);
}
