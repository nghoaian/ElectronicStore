package tdtu.edu.vn.service;

import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.Product;
import tdtu.edu.vn.repository.ProductRepository;

@Service
public class ProductService extends CRUDService<Product, Integer, ProductRepository>{
    public ProductService(ProductRepository repository) {
        super(repository);
    }
}
