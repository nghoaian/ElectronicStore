package tdtu.edu.vn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@SuppressWarnings("ALL")
@Entity
@Table(name = "order-item")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "int default 1")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private OrderItem(Builder builder) {
        this.id = builder.id;
        this.quantity = builder.quantity;
        this.order = builder.order;
        this.product = builder.product;
    }

    // use Prototype pattern
    @Override
    public OrderItem clone(){
        try {
            return (OrderItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // use Builder pattern to create an instance of OrderItem
    public static class Builder {
        private int id;
        private int quantity;
        private Order order;
        private Product product;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setOrder(Order order) {
            this.order = order;
            return this;
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
