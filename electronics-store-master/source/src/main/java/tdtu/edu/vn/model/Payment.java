package tdtu.edu.vn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("ALL")
@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(columnDefinition = "varchar(255)")
    private String time;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @PrePersist
    public void prePersist() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.time = sdf.format(new Date());
    }

    private Payment(Builder builder){
        this.id = builder.id;
        this.paymentMethod = builder.paymentMethod;
        this.time = builder.time;
        this.order = builder.order;
    }

    // use Prototype pattern
    @Override
    public Payment clone(){
        try {
            return (Payment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // use Builder pattern to create an instance of Payment
    public static class Builder {
        private int id;
        private PaymentMethod paymentMethod;
        private String time;
        private Order order;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setPaymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder setTime(String time) {
            this.time = time;
            return this;
        }

        public Builder setOrder(Order order) {
            this.order = order;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }

    public enum PaymentMethod {
        CASH, BANK_TRANSFER
    }
}
