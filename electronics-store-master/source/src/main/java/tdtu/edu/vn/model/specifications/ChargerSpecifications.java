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
@DiscriminatorValue("CHARGER")
public class ChargerSpecifications extends Specifications{
    private String type;
    private String interfacePort;
    private String features;
    private String compatibility;

    private ChargerSpecifications(Builder builder){
        this.type = builder.type;
        this.interfacePort = builder.interfacePort;
        this.features = builder.features;
        this.compatibility = builder.compatibility;
    }

    // use Prototype pattern
    @Override
    public ChargerSpecifications clone(){
        try {
            return (ChargerSpecifications) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Builder {
        private String type;
        private String interfacePort;
        private String features;
        private String compatibility;

        public Builder setType(String type) {
            this.type = type;
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

        public Builder setCompatibility(String compatibility) {
            this.compatibility = compatibility;
            return this;
        }

        public ChargerSpecifications build(){
            return new ChargerSpecifications(this);
        }
    }
}
