package tdtu.edu.vn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "varchar(255)")
    private String orderDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CART;
    private String address;
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    public void prePersist() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.orderDate = sdf.format(new Date());
    }

    private Order(Builder builder){
        this.id = builder.id;
        this.orderDate = builder.orderDate;
        this.status = builder.status;
        this.address = builder.address;
        this.totalPrice = builder.totalPrice;
        this.user = builder.user;
        this.orderItems = builder.orderItems;
        this.payment = builder.payment;
    }

    // use Prototype pattern to clone an Order object
    @Override
    public Order clone(){
        try {
            return (Order) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // use Builder pattern to create an Order object
    public static class Builder{
        private int id;
        private String orderDate;
        private Status status;
        private String address;
        private int totalPrice;
        private User user;
        private List<OrderItem> orderItems;
        private Payment payment;

        public Builder setId(int id){
            this.id = id;
            return this;
        }

        public Builder setOrderDate(String orderDate){
            this.orderDate = orderDate;
            return this;
        }

        public Builder setStatus(Status status){
            this.status = status;
            return this;
        }

        public Builder setAddress(String address){
            this.address = address;
            return this;
        }

        public Builder setTotalPrice(int totalPrice){
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder setUser(User user){
            this.user = user;
            return this;
        }

        public Builder setOrderItems(List<OrderItem> orderItems){
            this.orderItems = orderItems;
            return this;
        }

        public Builder setPayment(Payment payment){
            this.payment = payment;
            return this;
        }

        public Order build(){
            return new Order(this);
        }
    }

    public enum Status {
        CART, DELIVERED, COMPLETED, CANCELLED, RETURNED
    }
}
