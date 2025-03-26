package tdtu.edu.vn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@SuppressWarnings("ALL")
@Entity
@Table(name = "evaluation")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int evaluationPoint;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Evaluation(Builder builder){
        this.id = builder.id;
        this.content = builder.content;
        this.evaluationPoint = builder.evaluationPoint;
        this.user = builder.user;
        this.product = builder.product;
    }

    public static class Builder {
        private int id;
        private String content;
        private int evaluationPoint;
        private User user;
        private Product product;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setEvaluationPoint(int evaluationPoint) {
            this.evaluationPoint = evaluationPoint;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Evaluation build() {
            return new Evaluation(this);
        }
    }
}
