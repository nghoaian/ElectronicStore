package tdtu.edu.vn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import tdtu.edu.vn.model.specifications.Specifications;

import java.util.List;

@SuppressWarnings("ALL")
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(columnDefinition = "text")
    private String images;

    private int price;
    private String category;
    private String brand;

    @Column(columnDefinition = "double default 0.0")
    private double evaluationPoint;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Evaluation> evaluations;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    private Specifications specifications;

    private Product(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.images = builder.images;
        this.price = builder.price;
        this.category = builder.category;
        this.brand = builder.brand;
        this.evaluationPoint = builder.evaluationPoint;
        this.orderItems = builder.orderItems;
        this.evaluations = builder.evaluations;
        this.specifications = builder.specifications;
    }

    // use Prototype pattern
    @Override
    public Product clone(){
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // use Builder pattern to create an instance of Product
    public static class Builder{
        private int id;
        private String name;
        private String images;
        private int price;
        private String category;
        private String brand;
        private double evaluationPoint;
        private List<OrderItem> orderItems;
        private List<Evaluation> evaluations;
        private Specifications specifications;

        public Builder setId(int id){
            this.id = id;
            return this;
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setImages(String images){
            this.images = images;
            return this;
        }

        public Builder setPrice(int price){
            this.price = price;
            return this;
        }

        public Builder setCategory(String category){
            this.category = category;
            return this;
        }

        public Builder setBrand(String brand){
            this.brand = brand;
            return this;
        }

        public Builder setEvaluationPoint(double evaluationPoint){
            this.evaluationPoint = evaluationPoint;
            return this;
        }

        public Builder setOrderItems(List<OrderItem> orderItems){
            this.orderItems = orderItems;
            return this;
        }

        public Builder setEvaluations(List<Evaluation> evaluations){
            this.evaluations = evaluations;
            return this;
        }

        public Builder setSpecifications(Specifications specifications){
            this.specifications = specifications;
            return this;
        }

        public Product build(){
            return new Product(this);
        }
    }
}
