package tdtu.edu.vn.service;

import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.OrderItem;
import tdtu.edu.vn.repository.OrderItemRepository;

import java.util.List;

@Service
public class OrderItemService extends CRUDService<OrderItem, Integer, OrderItemRepository>{
    public OrderItemService(OrderItemRepository repository) {
        super(repository);
    }

    public void deleteByOrderId(Integer orderId){
        repository.deleteByOrderId(orderId);
    }

    public void deleteByProductId(Integer productId){
        repository.deleteByProductId(productId);
    }

    public List<OrderItem> findByOrderId(Integer orderId){
        return repository.findByOrderId(orderId);
    }

    public OrderItem findByOrderIdAndProductId(Integer orderId, Integer productId){
        return repository.findByOrderIdAndProductId(orderId, productId);
    }
}
