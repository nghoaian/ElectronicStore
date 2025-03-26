package tdtu.edu.vn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@SuppressWarnings("ALL")
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "varchar(255) default 'user'")
    private String username;
    private String email;
    private String phone;

    @Column(columnDefinition = "varchar(255) default 'MALE'")
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.MALE;
    private String dob;
    private String password;

    @Column(columnDefinition = "varchar(255) default 'USER'")
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    private boolean isLocked = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Evaluation> evaluations;

    public enum Gender{
        MALE, FEMALE
    }

    public enum Role{
        USER, ADMIN
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.phone = builder.phone;
        this.gender = builder.gender;
        this.dob = builder.dob;
        this.password = builder.password;
        this.role = builder.role;
        this.isLocked = builder.isLocked;
        this.orders = builder.orders;
        this.evaluations = builder.evaluations;
    }

    // use Prototype
    @Override
    public User clone(){
        try {
            return (User) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // use Builder pattern to create an instance of User
    public static class Builder {
        private int id;
        private String username;
        private String email;
        private String phone;
        private Gender gender;
        private String dob;
        private String password;
        private Role role;
        private boolean isLocked;
        private List<Order> orders;
        private List<Evaluation> evaluations;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setGender(Gender gender){
            this.gender = gender;
            return this;
        }

        public Builder setDob(String dob) {
            this.dob = dob;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setIsLocked(boolean isLocked) {
            this.isLocked = isLocked;
            return this;
        }

        public Builder setOrders(List<Order> orders) {
            this.orders = orders;
            return this;
        }

        public Builder setEvaluations(List<Evaluation> evaluations) {
            this.evaluations = evaluations;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
