package tdtu.edu.vn.util.CRUDHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.Evaluation;
import tdtu.edu.vn.model.Product;
import tdtu.edu.vn.model.User;
import tdtu.edu.vn.payload.ResponseData;
import tdtu.edu.vn.repository.EvaluationRepository;
import tdtu.edu.vn.service.CRUDService;
import tdtu.edu.vn.service.ProductService;
import tdtu.edu.vn.service.UserService;

import java.util.List;

@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CRUDEvaluation extends CRUD<Evaluation, EvaluationRepository> {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public CRUDEvaluation(CRUDService<Evaluation, Integer, EvaluationRepository> crudService) {
        super(crudService);
    }


    @Override
    public ResponseEntity<?> create(Evaluation entity) {
        try {
            if (entity == null) {
                return ResponseEntity.badRequest().body("Cannot create evaluation. Please provide information!");
            }

            if(entity.getUser() == null)
                return ResponseEntity.badRequest().body("Cannot create evaluation. Please provide user information!");
            if (entity.getProduct() == null)
                return ResponseEntity.badRequest().body("Cannot create evaluation. Please provide product information!");

            ResponseData responseData = new ResponseData.Builder()
                    .data(crudService.save(entity))
                    .message("Create evaluation successfully!")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create evaluation failed!");
        }
    }

    @Override
    public ResponseEntity<?> update(Evaluation entity) {
        try {
            if (entity == null) {
                return ResponseEntity.badRequest().body("Cannot update evaluation. Please provide information!");
            }

            Evaluation evaluation = crudService.findById(entity.getId());
            if (evaluation == null) {
                return ResponseEntity.badRequest().body("Evaluation not found!");
            }

            User user = userService.findById(evaluation.getUser().getId());
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found!");
            }

            Product product = productService.findById(evaluation.getProduct().getId());
            if (product == null) {
                return ResponseEntity.badRequest().body("Product not found!");
            }

            if (!entity.getContent().isEmpty())
                evaluation.setContent(entity.getContent());
            if (entity.getEvaluationPoint() != 0)
                evaluation.setEvaluationPoint(entity.getEvaluationPoint());

            return ResponseEntity.ok(new ResponseData(crudService.update(evaluation), "Create evaluation successfully!"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update evaluation failed!");
        }
    }

    @Override
    public ResponseEntity<?> search(Evaluation entity) {
        try {
            List<Evaluation> evaluations = crudService.findAll();
            evaluations = evaluations.stream()
                    .filter(evaluation -> (evaluation.getUser().getId() == entity.getUser().getId() || entity.getUser().getId() == 0) &&
                            (entity.getContent() == null || evaluation.getContent().contains(entity.getContent())) &&
                            (evaluation.getEvaluationPoint() == entity.getEvaluationPoint() || entity.getEvaluationPoint() == 0))
                    .toList();

            if (evaluations.isEmpty()) {
                return ResponseEntity.ok("No evaluation found with the given information");
            }


            ResponseData responseData = new ResponseData.Builder()
                    .data(evaluations)
                    .message("Search evaluation successfully!")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Search order failed!");
        }
    }
}
