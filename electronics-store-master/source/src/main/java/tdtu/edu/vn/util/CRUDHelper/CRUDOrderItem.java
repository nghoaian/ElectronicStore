package tdtu.edu.vn.util.CRUDHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.model.OrderItem;
import tdtu.edu.vn.model.Product;
import tdtu.edu.vn.payload.ResponseData;
import tdtu.edu.vn.repository.OrderItemRepository;
import tdtu.edu.vn.service.CRUDService;
import tdtu.edu.vn.service.OrderItemService;
import tdtu.edu.vn.service.OrderService;
import tdtu.edu.vn.service.ProductService;

import java.util.List;

@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CRUDOrderItem extends CRUD<OrderItem, OrderItemRepository> {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    public CRUDOrderItem(CRUDService<OrderItem, Integer, OrderItemRepository> crudService) {
        super(crudService);
    }

    @Override
    public ResponseEntity<?> create(OrderItem entity) {
        try {
            if (entity == null)
                return ResponseEntity.badRequest().body("Cannot create order-item. Please provide information!");
            Order order = orderService.findById(entity.getOrder().getId());
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

            Product product = productService.findById(entity.getProduct().getId());
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }

            OrderItemService orderItemService = (OrderItemService) crudService;

            OrderItem orderItem = new OrderItem.Builder()
                    .setQuantity(entity.getQuantity())
                    .setOrder(order)
                    .setProduct(product)
                    .build();

            return ResponseEntity.ok(new ResponseData(orderItemService.save(orderItem), "Create order-item successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Create order-item failed!");
        }
    }

    @Override
    public ResponseEntity<?> update(OrderItem entity) {
        try {
            if (entity == null) {
                return ResponseEntity.badRequest().body("Cannot update order-item. Please provide information!");
            }

            OrderItem getEntity;
            if(entity.getId() != 0)
                getEntity = crudService.findById(entity.getId());
            else if(entity.getOrder() != null && entity.getProduct() != null)
                getEntity = ((OrderItemService) crudService).findByOrderIdAndProductId(entity.getOrder().getId(), entity.getProduct().getId());
            else {
                return ResponseEntity.badRequest().body("Cannot update order-item. Please provide information!");
            }

            if (getEntity == null) {
                return create(entity);
            }

            if(entity.getQuantity() <= 0){
                crudService.delete(entity);
                return ResponseEntity.ok(new ResponseData(null, "Quantity is less than or equals zeros. Delete order-item successfully!"));
            }

            assert entity.getOrder() != null;
            Order order = orderService.findById(getEntity.getOrder().getId());
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }
            entity.setOrder(getEntity.getOrder());

            Product product = productService.findById(getEntity.getProduct().getId());
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
            entity.setProduct(getEntity.getProduct());
            entity.setId(getEntity.getId());

            ResponseData responseData = new ResponseData.Builder()
                    .data(crudService.update(entity))
                    .message("Update order-item successfully!")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update order-item failed!");
        }
    }

    @Override
    public ResponseEntity<?> search(OrderItem entity) {
        try {
            List<OrderItem> orderItems = crudService.findAll();
            orderItems = orderItems.stream().filter(orderItem ->
                            (entity.getQuantity() == 0 || orderItem.getQuantity() == entity.getQuantity()) &&
                                    (entity.getOrder().getId() == 0 || orderItem.getOrder().getId() == entity.getOrder().getId()) &&
                                    (entity.getProduct().getId() == 0 || orderItem.getProduct().getId() == entity.getProduct().getId()))
                    .toList();

            if (orderItems.isEmpty())
                return ResponseEntity.ok("Order-item not found!");

            ResponseData responseData = new ResponseData.Builder()
                    .data(orderItems)
                    .message("Search order-item successfully!")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Search order-item failed!");
        }
    }
}
