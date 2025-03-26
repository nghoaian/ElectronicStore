package tdtu.edu.vn.controller;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.edu.vn.model.Evaluation;
import tdtu.edu.vn.model.OrderItem;
import tdtu.edu.vn.model.Product;
import tdtu.edu.vn.service.OrderItemService;
import tdtu.edu.vn.util.CRUDDecoratorHelper.JwtAuthorization;
import tdtu.edu.vn.util.CRUDHelper.CRUDEvaluation;
import tdtu.edu.vn.util.CRUDHelper.CRUDOrderItem;
import tdtu.edu.vn.util.CRUDHelper.CRUDProduct.ProductSpecification;
import tdtu.edu.vn.util.CRUDHelper.CRUDProductAdapter;

import static tdtu.edu.vn.util.CRUDHelper.CRUDType.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@AllArgsConstructor
@RequestMapping("/api/detail")
@CrossOrigin("http://localhost:3000")
public class DetailController {
    @Autowired(required = false)
    private JwtAuthorization<?> jwtAuthorization;
    private final CRUDProductAdapter crudProduct;
    private final CRUDEvaluation crudEvaluation;
    private final CRUDOrderItem crudOrderItem;
    private final OrderItemService orderItemService;

    @PostConstruct
    public void init() {
        this.jwtAuthorization.setWrapObjectCrud(crudProduct);
    }

    @PostMapping("")
    public ResponseEntity<?> detail(@RequestBody ProductSpecification productSpecification) {
        // to make sure that the jwtAuthorization is not null
        this.jwtAuthorization.setWrapObjectCrud(crudProduct);
        return jwtAuthorization.executeFactory(productSpecification, SHOW);
    }

    @PostMapping("/evaluation")
    public ResponseEntity<?> evaluation(@RequestBody Evaluation evaluation){
        this.jwtAuthorization.setWrapObjectCrud(crudEvaluation);
        return jwtAuthorization.executeFactory(evaluation, CREATE);
    }

    @PostMapping("/get-evaluation")
    public ResponseEntity<?> getEvaluation(@RequestBody Evaluation evaluation){
        this.jwtAuthorization.setWrapObjectCrud(crudEvaluation);
        return jwtAuthorization.executeFactory(evaluation, SEARCH);
    }

    @PostMapping("/update-evaluation")
    public ResponseEntity<?> updateEvaluation(@RequestBody Evaluation evaluation){
        this.jwtAuthorization.setWrapObjectCrud(crudEvaluation);
        return jwtAuthorization.executeFactory(evaluation, UPDATE);
    }

    @PostMapping("/delete-evaluation")
    public ResponseEntity<?> deleteEvaluation(@RequestBody Evaluation evaluation){
        this.jwtAuthorization.setWrapObjectCrud(crudEvaluation);
        if (evaluation.getUser() != null)
            evaluation.setUser(null);
        if (evaluation.getProduct() != null)
            evaluation.setProduct(null);
        return jwtAuthorization.executeFactory(evaluation, DELETE);
    }

    @PostMapping("/show-evaluation")
    public ResponseEntity<?> showEvaluation(@RequestBody Evaluation evaluation){
        this.jwtAuthorization.setWrapObjectCrud(crudEvaluation);
        return jwtAuthorization.executeFactory(evaluation, SHOW);
    }

    @PostMapping("/order")
    public ResponseEntity<?> order(@RequestBody Product product, @RequestParam("quantity") int quantity){
        this.jwtAuthorization.setWrapObjectCrud(crudOrderItem);


        System.out.println(quantity);
        OrderItem orderItem = new OrderItem.Builder()
                .setProduct(product)
                .setQuantity(quantity)
                .build();

        return jwtAuthorization.executeFactory(orderItem, UPDATE);
    }
}
