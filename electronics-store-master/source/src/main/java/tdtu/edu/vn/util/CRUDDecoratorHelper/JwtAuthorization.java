package tdtu.edu.vn.util.CRUDDecoratorHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.*;
import tdtu.edu.vn.repository.UserRepository;
import tdtu.edu.vn.service.OrderItemService;
import tdtu.edu.vn.service.OrderService;
import tdtu.edu.vn.util.CRUDHelper.CRUDInterfaces;
import tdtu.edu.vn.util.CRUDHelper.CRUDType;
import tdtu.edu.vn.util.JwtUtilsHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static tdtu.edu.vn.model.Order.Status.*;

@Service
@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection"})
public class JwtAuthorization<T> extends CRUDAuthorization<T> {
    @Autowired
    HttpServletRequest request;
    @Autowired
    JwtUtilsHelper jwtUtilsHelper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    public JwtAuthorization() {
        super(null);
    }

    @SuppressWarnings("unchecked")
    public void setWrapObjectCrud(CRUDInterfaces<?> wrapObjectCrud) {
        super.wrapObjectCrud = (CRUDInterfaces<T>) wrapObjectCrud;
    }

    @Override
    User getUserFromRequest(T entity) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            String phoneNumber = jwtUtilsHelper.getPhoneNumberFromToken(token);
            User user = userRepository.findByPhone(phoneNumber);

            if (entity.getClass() == Order.class) {
                // to reference the cart of user
                ((Order) entity).setUser(user);
                Order cart = orderService.findByUserIdAndOrderStatus(user.getId(), CART);
                if (cart != null) {
                    ((Order) entity).setId(cart.getId());
                }
            } else if (entity.getClass() == Evaluation.class){
                ((Evaluation) entity).setUser(user);
            } else if (entity.getClass() == OrderItem.class) {
                Order order = orderService.findByUserIdAndOrderStatus(user.getId(), CART);

                // automatic calculate total price of order
                if (order != null) {
                    List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
                    order.setTotalPrice(
                            orderItems.stream().mapToInt(o -> o.getQuantity() * o.getProduct().getPrice()).sum()
                    );
                    try {
                        orderService.update(order);
                    } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                        System.out.println("Error with: " + e.getMessage());
                        return null;
                    }
                }

                ((OrderItem) entity).setOrder(order);
            } else if (entity.getClass() == Payment.class) {
                ((Payment) entity).setOrder(orderService.findByUserIdAndOrderStatus(user.getId(), CART));
            } else if (entity.getClass() == User.class){
                System.out.println("User: " + user.getId());
                if (((User) entity).getId() == 0)
                    ((User) entity).setId(user.getId());
            }
            return user;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public ResponseEntity<?> executeFactory(Object entity, CRUDType type) {
        return switch (type) {
            case CREATE -> create((T) entity);
            case UPDATE -> update((T) entity);
            case DELETE -> delete((T) entity);
            case SHOW -> show((T) entity);
            case SEARCH -> search((T) entity);
        };
    }
}
