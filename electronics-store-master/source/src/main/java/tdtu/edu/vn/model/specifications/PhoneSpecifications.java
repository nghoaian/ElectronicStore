package tdtu.edu.vn.model.specifications;

import jakarta.persistence.*;
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
@DiscriminatorValue("PHONE")
public class PhoneSpecifications extends Specifications{
    private String screen;
    private String rearCamera;
    private String frontCamera;
    private String RAM;
    private String internalProcessor;
    private String CPU;
    private String GPU;
    private String batteryCapacity;
    private String simCard;
    private String operatingSystem;
    private String origin;
    private String releaseDate;

    private PhoneSpecifications(Builder builder){
        this.screen = builder.screen;
        this.rearCamera = builder.rearCamera;
        this.frontCamera = builder.frontCamera;
        this.RAM = builder.RAM;
        this.internalProcessor = builder.internalProcessor;
        this.CPU = builder.CPU;
        this.GPU = builder.GPU;
        this.batteryCapacity = builder.batteryCapacity;
        this.simCard = builder.simCard;
        this.operatingSystem = builder.operatingSystem;
        this.origin = builder.origin;
        this.releaseDate = builder.releaseDate;
    }

    // use Prototype pattern
    @Override
    public PhoneSpecifications clone(){
        try {
            return (PhoneSpecifications) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Builder {
        private String screen;
        private String rearCamera;
        private String frontCamera;
        private String RAM;
        private String internalProcessor;
        private String CPU;
        private String GPU;
        private String batteryCapacity;
        private String simCard;
        private String operatingSystem;
        private String origin;
        private String releaseDate;

        public void setScreen(String screen) {
            this.screen = screen;
        }

        public void setRearCamera(String rearCamera) {
            this.rearCamera = rearCamera;
        }

        public void setFrontCamera(String frontCamera) {
            this.frontCamera = frontCamera;
        }

        public void setRAM(String RAM) {
            this.RAM = RAM;
        }

        public void setInternalProcessor(String internalProcessor) {
            this.internalProcessor = internalProcessor;
        }

        public void setCPU(String CPU) {
            this.CPU = CPU;
        }

        public void setGPU(String GPU) {
            this.GPU = GPU;
        }

        public void setBatteryCapacity(String batteryCapacity) {
            this.batteryCapacity = batteryCapacity;
        }

        public void setSimCard(String simCard) {
            this.simCard = simCard;
        }

        public void setOperatingSystem(String operatingSystem) {
            this.operatingSystem = operatingSystem;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public PhoneSpecifications build() {
            return new PhoneSpecifications(this);
        }
    }
}
