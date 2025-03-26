package tdtu.edu.vn.model.specifications;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("ALL")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@DiscriminatorValue("MOUSE")
public class MouseSpecifications extends Specifications{
    private String type;
    private String connectivity;
    private String origin;
    private String warrantyPeriod;

    private MouseSpecifications(Builder builder){
        this.type = builder.type;
        this.connectivity = builder.connectivity;
        this.origin = builder.origin;
        this.warrantyPeriod = builder.warrantyPeriod;
    }

    // use Prototype pattern
    @Override
    public MouseSpecifications clone(){
        try {
            return (MouseSpecifications) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Builder {
        private String type;
        private String connectivity;
        private String origin;
        private String warrantyPeriod;

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setConnectivity(String connectivity) {
            this.connectivity = connectivity;
            return this;
        }

        public Builder setOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public Builder setWarrantyPeriod(String warrantyPeriod) {
            this.warrantyPeriod = warrantyPeriod;
            return this;
        }

        public MouseSpecifications build(){
            return new MouseSpecifications(this);
        }
    }
}
