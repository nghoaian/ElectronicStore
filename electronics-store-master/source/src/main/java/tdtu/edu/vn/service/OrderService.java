package tdtu.edu.vn.service;

import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.repository.OrderRepository;

@Service
public class OrderService extends CRUDService<Order, Integer, OrderRepository>{
    public OrderService(OrderRepository repository) {
        super(repository);
    }

    public Order findByUserIdAndOrderStatus(int userId, Order.Status status) {
        return repository.findByUserIdAndOrderStatus(userId, status);
    }
}
