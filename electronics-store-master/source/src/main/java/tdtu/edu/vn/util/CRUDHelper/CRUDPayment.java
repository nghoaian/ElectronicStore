package tdtu.edu.vn.util.CRUDHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.model.Payment;
import tdtu.edu.vn.payload.ResponseData;
import tdtu.edu.vn.repository.PaymentRepository;
import tdtu.edu.vn.service.CRUDService;
import tdtu.edu.vn.service.OrderService;

import java.util.List;

@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CRUDPayment extends CRUD<Payment, PaymentRepository> {
    @Autowired
    private OrderService orderService;

    public CRUDPayment(CRUDService<Payment, Integer, PaymentRepository> crudService) {
        super(crudService);
    }

    @Override
    public ResponseEntity<?> create(Payment entity) {
        try {
            if (entity == null) {
                return ResponseEntity.badRequest().body("Create payment failed. Please provide information!");
            }

            Order order = orderService.findById(entity.getOrder().getId());
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }
            entity.setOrder(order);

            if (entity.getPaymentMethod() == null)
                return ResponseEntity.badRequest().body("Payment method is required!");

            return ResponseEntity.ok(new ResponseData(crudService.save(entity), "Create payment successfully!"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Create payment failed!");
        }
    }

    @Override
    public ResponseEntity<?> update(Payment entity) {
        try {
            if (entity == null) {
                return ResponseEntity.badRequest().body("Create payment failed. Please provide information!");
            }

            Payment getEntity = crudService.findById(entity.getId());
            if (getEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
            }

            Order order = orderService.findById(entity.getOrder().getId());
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

            if (entity.getPaymentMethod() != null)
                getEntity.setPaymentMethod(entity.getPaymentMethod());
            if (entity.getTime() != null)
                getEntity.setTime(entity.getTime());

            ResponseData responseData = new ResponseData.Builder()
                    .data(crudService.update(entity))
                    .message("Update successfully")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> search(Payment entity) {
        try {
            List<Payment> payments = crudService.findAll();
            payments = payments.stream()
                    .filter(payment -> (entity.getId() == 0 || payment.getId() == entity.getId()) &&
                            (entity.getPaymentMethod() == null || payment.getPaymentMethod().equals(entity.getPaymentMethod())) &&
                            (entity.getTime() == null || payment.getTime().contains(entity.getTime())) &&
                            (entity.getOrder().getId() == 0 || payment.getOrder().getId() == entity.getOrder().getId()))
                    .toList();

            if(payments.isEmpty()){
                return ResponseEntity.ok("Search payment failed!");
            }

            ResponseData responseData = new ResponseData.Builder()
                    .data(payments)
                    .message("Search payment successfully!")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Search payment failed!");
        }
    }
}
