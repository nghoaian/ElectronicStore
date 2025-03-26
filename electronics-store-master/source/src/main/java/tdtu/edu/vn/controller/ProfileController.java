package tdtu.edu.vn.controller;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.model.User;
import tdtu.edu.vn.service.ProductService;
import tdtu.edu.vn.util.CRUDDecoratorHelper.JwtAuthorization;
import tdtu.edu.vn.util.CRUDHelper.CRUDContext;
import tdtu.edu.vn.util.CRUDHelper.CRUDOrder;
import tdtu.edu.vn.util.CRUDHelper.CRUDProductAdapter;
import tdtu.edu.vn.util.CRUDHelper.CRUDUser;

import static tdtu.edu.vn.util.CRUDHelper.CRUDType.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@AllArgsConstructor
@RequestMapping("/api/profile")
@CrossOrigin("http://localhost:3000")
public class ProfileController {
    @Autowired(required = false)
    private JwtAuthorization<?> jwtAuthorization;
    private final ProductService productService;
    @Autowired(required = false)
    private CRUDContext crudContext;
    private final CRUDUser crudUser;
    private final CRUDOrder crudOrder;

    @PostConstruct
    public void init() {
        this.crudContext = new CRUDContext(crudUser);
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
    }

    @PostMapping("")
    public ResponseEntity<?> profile(@RequestBody User user) {
        // to make sure that the jwtAuthorization is not null
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
        return jwtAuthorization.executeFactory(user, SHOW);
    }

    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody User user) {
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
        return jwtAuthorization.executeFactory(user, UPDATE);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody User user) {
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
        if(user.getPassword().isEmpty())
            return ResponseEntity.badRequest().body("New password is required");
        return jwtAuthorization.executeFactory(user, UPDATE);
    }

    @PostMapping("/view-order")
    public ResponseEntity<?> viewOrder(@RequestBody Order order) {
        this.jwtAuthorization.setWrapObjectCrud(crudOrder);
        return jwtAuthorization.executeFactory(order, SEARCH);
    }
}
