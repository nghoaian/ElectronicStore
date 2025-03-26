package tdtu.edu.vn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdtu.edu.vn.model.Product;
import tdtu.edu.vn.service.ProductService;
import tdtu.edu.vn.util.CRUDDecoratorHelper.JwtAuthorization;
import tdtu.edu.vn.util.CRUDHelper.CRUDContext;
import tdtu.edu.vn.util.CRUDHelper.CRUDProduct.ProductSpecification;
import tdtu.edu.vn.util.CRUDHelper.CRUDProductAdapter;

import java.util.ArrayList;
import java.util.List;

import static tdtu.edu.vn.util.CRUDHelper.CRUDType.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@AllArgsConstructor
@RequestMapping("/api/home")
@CrossOrigin("http://localhost:3000")
public class HomeController {
    @Autowired(required = false)
    private JwtAuthorization<?> jwtAuthorization;
    private final ProductService productService;
    @Autowired(required = false)
    private CRUDContext crudContext;
    private final CRUDProductAdapter crudProduct;

    @PostConstruct
    public void init() {
        this.crudContext = new CRUDContext(crudProduct);
        this.jwtAuthorization.setWrapObjectCrud(crudProduct);
    }

    @PostMapping("")
    public ResponseEntity<?> home(@RequestBody ProductSpecification productSpecification) {
        // to make sure that the jwtAuthorization is not null
        this.jwtAuthorization.setWrapObjectCrud(crudProduct);
        return jwtAuthorization.executeFactory(productSpecification, SEARCH);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword) {
        List<Product> products = productService.findAll();
        List<ProductSpecification> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        for (Product product : products) {
            try {
                String json = mapper.writeValueAsString(product);
                if (json.toLowerCase().contains(keyword.toLowerCase())) {
                    ProductSpecification productSpecification = new ProductSpecification.Builder()
                            .setProduct(product)
                            .setType(product.getSpecifications().getSpecificationsType())
                            .setSpecifications(product.getSpecifications())
                            .build();
                    result.add(productSpecification);
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(result);
    }
}
