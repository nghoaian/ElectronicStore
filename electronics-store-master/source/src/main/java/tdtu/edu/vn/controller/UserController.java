package tdtu.edu.vn.controller;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tdtu.edu.vn.model.Order;
import tdtu.edu.vn.model.User;
import tdtu.edu.vn.payload.ResponseData;
import tdtu.edu.vn.service.EmailService;
import tdtu.edu.vn.service.UserService;
import tdtu.edu.vn.util.CRUDDecoratorHelper.JwtAuthorization;
import tdtu.edu.vn.util.CRUDHelper.CRUDContext;
import tdtu.edu.vn.util.CRUDHelper.CRUDOrder;
import tdtu.edu.vn.util.CRUDHelper.CRUDUser;
import tdtu.edu.vn.util.JwtUtilsHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static tdtu.edu.vn.model.Order.Status.*;
import static tdtu.edu.vn.util.CRUDHelper.CRUDType.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final CRUDUser crudUser;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtilsHelper jwtUtilsHelper;
    @Autowired(required = false)
    private CRUDContext crudContext;
    @Autowired(required = false)
    private JwtAuthorization<?> jwtAuthorization;
    private CRUDOrder crudOrder;

    @PostConstruct
    public void init() {
        this.crudContext = new CRUDContext(crudUser);
        this.jwtAuthorization.setWrapObjectCrud(crudUser);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        this.crudContext.setCrud(crudUser);
        Object user1 = crudContext.executeFactory(user, CREATE).getBody();
        this.crudContext.setCrud(crudOrder);
        Object cart = crudContext.executeFactory(new Order.Builder().setUser(user).setStatus(CART).build(), CREATE).getBody();

        Map<String, Object> mapData = new HashMap<>();
        mapData.put("user", user1);
        mapData.put("cart", cart);

        ResponseData responseData = new ResponseData.Builder()
                .data(mapData)
                .message("User registered successfully")
                .build();

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            String phone = user.getPhone();
            String password = user.getPassword();

            User getUser = userService.findByPhoneNumber(phone);

            if (getUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            if (bCryptPasswordEncoder.matches(password, getUser.getPassword())) {
                String token = jwtUtilsHelper.generateToken(getUser.getPhone(), getUser.getEmail(), getUser.getRole().toString(), getUser.getId());
                ResponseEntity<?> responseEntity = crudContext.executeFactory(getUser, SHOW);

                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    Object data = responseEntity.getBody();
                    if (data instanceof ResponseData) {

                        Map<String, Object> mapData = new HashMap<>();
                        mapData.put("token", token);
                        mapData.put("data", ((ResponseData) data).getData());

                        ResponseData responseData = new ResponseData.Builder()
                                .data(mapData)
                                .message(((ResponseData) data).getMessage())
                                .build();

                        return ResponseEntity.ok(responseData);
                    }
                } else {
                    return responseEntity;
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid phone number or password");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody User user) {
        crudContext.setCrud(crudUser);
        user.setPassword(generatePassword());
        return crudContext.executeFactory(user, UPDATE);
    }

    private String generatePassword() {
        Random random = new Random();
        int length = 8;

        return random.ints(length, 0, 36)
                .mapToObj(i -> i < 10 ? String.valueOf(i) : String.valueOf((char) (i + 55)))
                .collect(Collectors.joining());
    }
}
