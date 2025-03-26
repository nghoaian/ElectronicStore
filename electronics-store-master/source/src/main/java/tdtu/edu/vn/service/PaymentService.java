package tdtu.edu.vn.service;

import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.Payment;
import tdtu.edu.vn.repository.PaymentRepository;

@Service
public class PaymentService extends CRUDService<Payment, Integer, PaymentRepository>{
    public PaymentService(PaymentRepository repository) {
        super(repository);
    }
}
