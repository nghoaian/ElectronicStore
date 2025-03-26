package tdtu.edu.vn.util.CRUDHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.model.User;
import tdtu.edu.vn.payload.ResponseData;
import tdtu.edu.vn.repository.OrderRepository;
import tdtu.edu.vn.service.CRUDService;
import tdtu.edu.vn.service.OrderItemService;
import tdtu.edu.vn.service.UserService;

import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CRUDOrder extends CRUD<Order, OrderRepository> {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderItemService orderItemService;

    public CRUDOrder(CRUDService<Order, Integer, OrderRepository> crudService) {
        super(crudService);
    }

    @Override
    public ResponseEntity<?> create(Order entity) {
        try {
            if (entity == null) {
                return ResponseEntity.badRequest().body("Cannot create order. Please provide information!");
            }

            User user;
            if(entity.getUser().getId() != 0) {
                user = userService.findById(entity.getUser().getId());
            } else if (!entity.getUser().getEmail().isEmpty()){
                user = userService.findByEmail(entity.getUser().getEmail());
            } else if (!entity.getUser().getPhone().isEmpty()) {
                user = userService.findByPhoneNumber(entity.getUser().getPhone());
            } else {
                return ResponseEntity.badRequest().body("Cannot create order. Please provide information!");
            }

            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
            }
            entity.setUser(user);
            entity.setOrderDate(String.valueOf(new Date()));

            ResponseData responseData = new ResponseData.Builder()
                    .data(crudService.save(entity))
                    .message("Create order successfully!")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create order failed!");
        }
    }

    @Override
    public ResponseEntity<?> update(Order entity) {
        try {
            if (entity == null) {
                return ResponseEntity.badRequest().body("Cannot update order. Please provide information!");
            }

            Order order = crudService.findById(entity.getId());
            if(order == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found!");
            }

            if (entity.getOrderDate() != null) order.setOrderDate(entity.getOrderDate());
            if (entity.getStatus() != null) order.setStatus(entity.getStatus());
            if (entity.getAddress() != null) order.setAddress(entity.getAddress());
            if (entity.getTotalPrice() != 0) order.setTotalPrice(entity.getTotalPrice());

            ResponseData responseData = new ResponseData.Builder()
                    .data(crudService.update(order))
                    .message("Update order successfully!")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update order failed!");
        }
    }

    @Override
    public ResponseEntity<?> delete(Order entity){
        try{
            if (entity == null) {
                return ResponseEntity.badRequest().body("Cannot delete order. Please provide information!");
            }

            Order getEntity = crudService.findById(entity.getId());
            if(getEntity == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

            orderItemService.deleteByOrderId(entity.getId());
            crudService.delete(getEntity);
            return ResponseEntity.ok("Delete order and item's order successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> search(Order entity) {
        try {
            List<Order> orders = crudService.findAll();
            orders = orders.stream().filter(order ->
                    (entity.getUser().getId() == 0 || order.getUser().getId() == entity.getUser().getId()) &&
                            (entity.getOrderDate() == null || order.getOrderDate().contains(entity.getOrderDate())) &&
                            (entity.getStatus() == null || order.getStatus().equals(entity.getStatus())) &&
                            (entity.getAddress() == null || order.getAddress().contains(entity.getAddress())) &&
                            (entity.getTotalPrice() == 0 || order.getTotalPrice() == entity.getTotalPrice()))
                    .toList();

            if (orders.isEmpty()) {
                return ResponseEntity.ok("Order not found with search information!");
            }

            ResponseData responseData = new ResponseData.Builder()
                    .data(orders)
                    .message("Search order successfully!")
                    .build();
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Search order failed!");
        }
    }
}
