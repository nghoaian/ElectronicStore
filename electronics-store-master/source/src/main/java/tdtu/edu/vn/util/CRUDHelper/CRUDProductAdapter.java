package tdtu.edu.vn.util.CRUDHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.util.CRUDHelper.CRUDProduct.ProductSpecification;

@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CRUDProductAdapter implements CRUDInterfaces<ProductSpecification> {
    @Autowired
    private CRUDProduct crudProduct;

    @Override
    public ResponseEntity<?> create(ProductSpecification entity) {
        return crudProduct.create(entity);
    }

    @Override
    public ResponseEntity<?> update(ProductSpecification entity) {
        return crudProduct.update(entity);
    }

    @Override
    public ResponseEntity<?> delete(ProductSpecification entity) {
        return crudProduct.delete(entity);
    }

    @Override
    public ResponseEntity<?> show(ProductSpecification entity) {
        return crudProduct.show(entity);
    }

    @Override
    public ResponseEntity<?> search(ProductSpecification entity) {
        return crudProduct.search(entity);
    }
}
