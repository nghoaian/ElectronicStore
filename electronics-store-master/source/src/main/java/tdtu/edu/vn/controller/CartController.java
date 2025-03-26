package tdtu.edu.vn.controller;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.model.OrderItem;
import tdtu.edu.vn.model.Payment;
import tdtu.edu.vn.util.CRUDDecoratorHelper.JwtAuthorization;
import tdtu.edu.vn.util.CRUDHelper.CRUDOrder;
import tdtu.edu.vn.util.CRUDHelper.CRUDOrderItem;
import tdtu.edu.vn.util.CRUDHelper.CRUDPayment;
import tdtu.edu.vn.util.CRUDHelper.CRUDProduct.*;
import tdtu.edu.vn.util.CRUDHelper.CRUDProductAdapter;

import static tdtu.edu.vn.model.Order.Status.*;
import static tdtu.edu.vn.util.CRUDHelper.CRUDType.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
@CrossOrigin("http://localhost:3000")
public class CartController {
    @Autowired(required = false)
    private JwtAuthorization<?> jwtAuthorization;
    private final CRUDOrder crudOrder;
    private final CRUDOrderItem crudOrderItem;
    private final CRUDPayment crudPayment;
    @PostConstruct
    public void init() {
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
    }

    @PostMapping("")
    public ResponseEntity<?> detail(@RequestBody Order order) {
        // to make sure that the jwtAuthorization is not null
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, SHOW);
    }

    @PostMapping("/update-item")
    public ResponseEntity<?> update(@RequestBody OrderItem orderItem) {
        // to make sure that the jwtAuthorization is not null
        this.jwtAuthorization.setWrapObjectCrud(crudOrderItem);
        return jwtAuthorization.executeFactory(orderItem, UPDATE);
    }

    @PostMapping("/payment")
    public ResponseEntity<?> payment(@RequestBody Payment payment) {
        this.jwtAuthorization.setWrapObjectCrud(crudPayment);
        return jwtAuthorization.executeFactory(payment, CREATE);
    }

    @PostMapping("/complete-payment")
    public ResponseEntity<?> completePayment(@RequestBody Payment payment) {
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        this.jwtAuthorization.executeFactory(new Order.Builder().setStatus(DELIVERED).build(), UPDATE);
        this.jwtAuthorization.executeFactory(new Order.Builder().setStatus(CART).build(), CREATE);
        this.jwtAuthorization.setWrapObjectCrud(crudPayment);
        return jwtAuthorization.executeFactory(payment, UPDATE);
    }
}
