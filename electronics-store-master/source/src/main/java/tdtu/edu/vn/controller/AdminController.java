package tdtu.edu.vn.controller;

import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.edu.vn.model.Evaluation;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.model.Payment;
import tdtu.edu.vn.model.User;
import tdtu.edu.vn.service.EvaluationService;
import tdtu.edu.vn.service.OrderItemService;
import tdtu.edu.vn.service.SpecificationsService;
import tdtu.edu.vn.util.CRUDDecoratorHelper.JwtAuthorization;
import tdtu.edu.vn.util.CRUDHelper.*;
import tdtu.edu.vn.util.CRUDHelper.CRUDProduct.ProductSpecification;

import static tdtu.edu.vn.util.CRUDHelper.CRUDType.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin("http://localhost:3000")
public class AdminController {
    @Autowired(required = false)
    private JwtAuthorization<?> jwtAuthorization;
    private final CRUDProductAdapter crudProduct;
    private final CRUDEvaluation crudEvaluation;
    private final OrderItemService orderItemService;
    private final EvaluationService evaluationService;
    private final SpecificationsService specificationsService;
    private final CRUDUser crudUser;
    private final CRUDOrder crudOrder;
    private final CRUDPayment crudPayment;

    @PostConstruct
    public void init(){
        this.jwtAuthorization.setWrapObjectCrud(crudProduct);
    }

    // for evaluation
    @PostMapping("/create-evaluation")
    public ResponseEntity<?> createEvaluation(@RequestBody Evaluation evaluation){
        this.jwtAuthorization.setWrapObjectCrud(crudEvaluation);
        return jwtAuthorization.executeFactory(evaluation, CREATE);
    }

    @PostMapping("/update-evaluation")
    public ResponseEntity<?> updateEvaluation(@RequestBody Evaluation evaluation){
        this.jwtAuthorization.setWrapObjectCrud(crudEvaluation);
        return jwtAuthorization.executeFactory(evaluation, UPDATE);
    }

    @PostMapping("/delete-evaluation")
    public ResponseEntity<?> deleteEvaluation(@RequestBody Evaluation evaluation){
        this.jwtAuthorization.setWrapObjectCrud(crudEvaluation);
        return jwtAuthorization.executeFactory(evaluation, DELETE);
    }

    @PostMapping("/search-evaluation")
    public ResponseEntity<?> searchEvaluation(@RequestBody Evaluation evaluation){
        this.jwtAuthorization.setWrapObjectCrud(crudEvaluation);
        return jwtAuthorization.executeFactory(evaluation, SEARCH);
    }

    @PostMapping("/crate-product")
    public ResponseEntity<?> createProduct(@RequestBody ProductSpecification productSpecification){
        this.jwtAuthorization.setWrapObjectCrud(crudProduct);
        return jwtAuthorization.executeFactory(productSpecification, CREATE);
    }

    @PostMapping("/update-product")
    public ResponseEntity<?> updateProduct(@RequestBody ProductSpecification productSpecification){
        this.jwtAuthorization.setWrapObjectCrud(crudProduct);
        return jwtAuthorization.executeFactory(productSpecification, UPDATE);
    }

    @PostMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@RequestBody ProductSpecification productSpecification){
        if (productSpecification.getProduct().getId() == 0)
            return ResponseEntity.badRequest().body("Product id is required");
        orderItemService.deleteByProductId(productSpecification.getProduct().getId());
        evaluationService.deleteByProductId(productSpecification.getProduct().getId());
        specificationsService.deleteByProductId(productSpecification.getProduct().getId());

        this.jwtAuthorization.setWrapObjectCrud(crudProduct);
        return jwtAuthorization.executeFactory(productSpecification, DELETE);
    }

    @PostMapping("/search-product")
    public ResponseEntity<?> searchProduct(@RequestBody ProductSpecification productSpecification){
        this.jwtAuthorization.setWrapObjectCrud(crudProduct);
        return jwtAuthorization.executeFactory(productSpecification, SEARCH);
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user){
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
        return jwtAuthorization.executeFactory(user, CREATE);
    }

    @PostMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
        return jwtAuthorization.executeFactory(user, UPDATE);
    }

    @PostMapping("delete-user")
    public ResponseEntity<?> deleteUser(@RequestBody User user){
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
        return jwtAuthorization.executeFactory(user, DELETE);
    }

    @PostMapping("/search-user")
    public ResponseEntity<?> searchUser(@RequestBody User user){
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
        return jwtAuthorization.executeFactory(user, SEARCH);
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Order order){
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, CREATE);
    }

    @PostMapping("/update-order")
    public ResponseEntity<?> updateOrder(@RequestBody Order order){
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, UPDATE);
    }

    @PostMapping("/delete-order")
    public ResponseEntity<?> deleteOrder(@RequestBody Order order){
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, DELETE);
    }

    @PostMapping("/search-order")
    public ResponseEntity<?> searchOrder(@RequestBody Order order){
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
        return jwtAuthorization.executeFactory(order, SEARCH);
    }

    @PostMapping("/view-order")
    public ResponseEntity<?> viewOrder(@RequestBody Order order){
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, SHOW);
    }

    @PostMapping("create-order-item")
    public ResponseEntity<?> createOrderItem(@RequestBody Order order){
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, CREATE);
    }

    @PostMapping("update-order-item")
    public ResponseEntity<?> updateOrderItem(@RequestBody Order order){
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, UPDATE);
    }

    @PostMapping("delete-order-item")
    public ResponseEntity<?> deleteOrderItem(@RequestBody Order order){
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, DELETE);
    }

    @PostMapping("search-order-item")
    public ResponseEntity<?> searchOrderItem(@RequestBody Order order){
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, SEARCH);
    }

    @PostMapping("update-payment")
    public ResponseEntity<?> updatePayment(@RequestBody Payment payment){
        this.jwtAuthorization.setWrapObjectCrud(crudPayment);
        return jwtAuthorization.executeFactory(payment, UPDATE);
    }

    @PostMapping("delete-payment")
    public ResponseEntity<?> deletePayment(@RequestBody Payment payment){
        this.jwtAuthorization.setWrapObjectCrud(crudPayment);
        return jwtAuthorization.executeFactory(payment, DELETE);
    }

    @PostMapping("search-payment")
    public ResponseEntity<?> searchPayment(@RequestBody Payment payment){
        this.jwtAuthorization.setWrapObjectCrud(crudPayment);
        return jwtAuthorization.executeFactory(payment, SEARCH);
    }
}
