package tdtu.edu.vn.service;

import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.User;
import tdtu.edu.vn.repository.UserRepository;

@Service
public class UserService extends CRUDService<User, Integer, UserRepository>{
    public UserService(UserRepository repository) {
        super(repository);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }
    public User findByPhoneNumber(String phoneNumber) {
        return repository.findByPhone(phoneNumber);
    }
}
