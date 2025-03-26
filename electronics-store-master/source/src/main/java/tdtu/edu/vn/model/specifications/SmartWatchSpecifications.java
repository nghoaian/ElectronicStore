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
@DiscriminatorValue("SMART_WATCH")
public class SmartWatchSpecifications extends Specifications{
    private String screen;
    private String internalProcessor;
    private String material;
    private String operatingSystem;
    private String batteryCapacity;
    private String warrantyPeriod;

    private SmartWatchSpecifications(Builder builder){
        this.screen = builder.screen;
        this.internalProcessor = builder.internalProcessor;
        this.material = builder.material;
        this.operatingSystem = builder.operatingSystem;
        this.batteryCapacity = builder.batteryCapacity;
        this.warrantyPeriod = builder.warrantyPeriod;
    }

    // use Prototype pattern
    @Override
    public SmartWatchSpecifications clone(){
        try {
            return (SmartWatchSpecifications) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Builder {
        private String screen;
        private String internalProcessor;
        private String material;
        private String operatingSystem;
        private String batteryCapacity;
        private String warrantyPeriod;

        public Builder setScreen(String screen) {
            this.screen = screen;
            return this;
        }

        public Builder setInternalProcessor(String internalProcessor) {
            this.internalProcessor = internalProcessor;
            return this;
        }

        public Builder setMaterial(String material) {
            this.material = material;
            return this;
        }

        public Builder setOperatingSystem(String operatingSystem) {
            this.operatingSystem = operatingSystem;
            return this;
        }

        public Builder setBatteryCapacity(String batteryCapacity) {
            this.batteryCapacity = batteryCapacity;
            return this;
        }

        public Builder setWarrantyPeriod(String warrantyPeriod) {
            this.warrantyPeriod = warrantyPeriod;
            return this;
        }

        public SmartWatchSpecifications build() {
            return new SmartWatchSpecifications(this);
        }
    }
}
