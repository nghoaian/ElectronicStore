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
@DiscriminatorValue("KEYBOARD")
public class KeyboardSpecifications extends Specifications{
    private String type;
    private String connectivity;
    private String interfacePort;
    private String features;
    private String operatingSystem;
    private String origin;
    private String warrantyPeriod;

    private KeyboardSpecifications(Builder builder){
        this.type = builder.type;
        this.connectivity = builder.connectivity;
        this.interfacePort = builder.interfacePort;
        this.features = builder.features;
        this.operatingSystem = builder.operatingSystem;
        this.origin = builder.origin;
        this.warrantyPeriod = builder.warrantyPeriod;
    }

    // use Prototype pattern
    @Override
    public KeyboardSpecifications clone(){
        try {
            return (KeyboardSpecifications) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Builder {
        private String type;
        private String connectivity;
        private String interfacePort;
        private String features;
        private String operatingSystem;
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

        public Builder setInterfacePort(String interfacePort) {
            this.interfacePort = interfacePort;
            return this;
        }

        public Builder setFeatures(String features) {
            this.features = features;
            return this;
        }

        public Builder setOperatingSystem(String operatingSystem) {
            this.operatingSystem = operatingSystem;
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

        public KeyboardSpecifications build(){
            return new KeyboardSpecifications(this);
        }
    }
}
