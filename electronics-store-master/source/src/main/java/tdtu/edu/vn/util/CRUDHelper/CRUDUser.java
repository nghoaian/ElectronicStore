package tdtu.edu.vn.util.CRUDHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.User;
import tdtu.edu.vn.payload.ResponseData;
import tdtu.edu.vn.repository.UserRepository;
import tdtu.edu.vn.service.EmailService;
import tdtu.edu.vn.service.UserService;

import java.util.List;


@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CRUDUser extends CRUD<User, UserRepository>{
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;

    public CRUDUser(UserService crudService) {
        super(crudService);
    }

    @Override
    public ResponseEntity<?> create(User entity) {
        try{
            if(entity == null) {
                return ResponseEntity.badRequest().body("Please provide user information");
            }

            String username = entity.getUsername();
            String email = entity.getEmail();
            String phone = entity.getPhone();
            String password = entity.getPassword();

            UserService userService = (UserService) crudService;
            if(userService.findByUsername(username) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("username is already used");
            }else if(userService.findByEmail(email) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("email is already used");
            }else if(userService.findByPhoneNumber(phone) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("phone number is already used");
            }


            User user = new User.Builder()
                    .setUsername(username)
                    .setEmail(email)
                    .setPhone(phone)
                    .setPassword(bCryptPasswordEncoder.encode(password))
                    .setRole(User.Role.USER)
                    .build();

            ResponseData responseData = new ResponseData.Builder()
                    .data(userService.save(user))
                    .message("Register successfully")
                    .build();

            return ResponseEntity.ok(responseData);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> update(User entity) {
        try{
            if(entity == null) {
                return ResponseEntity.badRequest().body("Please provide user information");
            }

            User getEntity;
            if (entity.getId() != 0)
                getEntity = crudService.findById(entity.getId());
            else if (entity.getPassword().isEmpty())
                getEntity = ((UserService) crudService).findByPhoneNumber(entity.getPhone());
            else
                return ResponseEntity.badRequest().body("Please provide user id or phone number");


            if(getEntity == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            if (entity.getUsername() != null)
                getEntity.setUsername(entity.getUsername());
            if (entity.getEmail() != null)
                getEntity.setEmail(entity.getEmail());
            if (entity.getPhone() != null)
                getEntity.setPhone(entity.getPhone());
            if (entity.getGender() != null)
                getEntity.setGender(entity.getGender());
            if (entity.getDob() != null)
                getEntity.setDob(entity.getDob());
            if (entity.getRole() != null)
                getEntity.setRole(entity.getRole());
            if (entity.isLocked() != getEntity.isLocked())
                getEntity.setLocked(entity.isLocked());

            if (entity.getPassword() != null)
                if(!bCryptPasswordEncoder.matches(entity.getPassword(), getEntity.getPassword())){
                    emailService.sendEmail("nguyntrungkin091@gmail.com", getEntity.getEmail(), "Your password is changed!", "Your password has been changed " + entity.getPassword() + " please contact us if you did not change it.");
                    entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
                }



            ResponseData responseData = new ResponseData.Builder()
                    .data(crudService.update(entity))
                    .message("Update successfully")
                    .build();
            return ResponseEntity.ok(responseData);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> search(User entity) {
        try {
            ResponseData.Builder responseData = new ResponseData.Builder();
            List<User> users = crudService.findAll();
            System.out.println(users);
            users = users.stream()
                    .filter(user -> (entity.getId() == 0 || user.getId() == entity.getId()) &&
                            (entity.getUsername() == null || user.getUsername().contains(entity.getUsername())) &&
                            (entity.getEmail() == null || user.getEmail().contains(entity.getEmail())) &&
                            (entity.getPhone() == null || user.getPhone().contains(entity.getPhone())) &&
                            (entity.getGender() == null || user.getGender().equals(entity.getGender())) &&
                            (entity.getDob() == null || user.getDob().contains(entity.getDob())) &&
                            (entity.getRole() == null || user.getRole().equals(entity.getRole())) &&
                            (user.isLocked() == entity.isLocked()))
                    .toList();

            if(users.isEmpty()){
                return ResponseEntity.ok("No user found with the given information");
            }else {
                responseData.data(users)
                        .message("Success to get data");
                return ResponseEntity.ok(responseData.build());
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
