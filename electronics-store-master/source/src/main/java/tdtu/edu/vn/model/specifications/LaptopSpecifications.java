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
@DiscriminatorValue("LAPTOP")
public class LaptopSpecifications  extends Specifications{
    private String screen;
    private String RAM;
    private String CPU;
    private String hardDrive;
    private String graphics;
    private String weight;
    private String dimensions;
    private String origin;
    private String manufacturerYear;

    private LaptopSpecifications(Builder builder){
        this.screen = builder.screen;
        this.RAM = builder.RAM;
        this.CPU = builder.CPU;
        this.hardDrive = builder.hardDrive;
        this.graphics = builder.graphics;
        this.weight = builder.weight;
        this.dimensions = builder.dimensions;
        this.origin = builder.origin;
        this.manufacturerYear = builder.manufacturerYear;
    }

    // use Prototype pattern
    @Override
    public LaptopSpecifications clone(){
        try {
            return (LaptopSpecifications) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Builder {
        private String screen;
        private String RAM;
        private String CPU;
        private String hardDrive;
        private String graphics;
        private String weight;
        private String dimensions;
        private String origin;
        private String manufacturerYear;

        public Builder setScreen(String screen) {
            this.screen = screen;
            return this;
        }

        public Builder setRAM(String RAM) {
            this.RAM = RAM;
            return this;
        }

        public Builder setCPU(String CPU) {
            this.CPU = CPU;
            return this;
        }

        public Builder setHardDrive(String hardDrive) {
            this.hardDrive = hardDrive;
            return this;
        }

        public Builder setGraphics(String graphics) {
            this.graphics = graphics;
            return this;
        }

        public Builder setWeight(String weight) {
            this.weight = weight;
            return this;
        }

        public Builder setDimensions(String dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder setOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public Builder setManufacturerYear(String manufacturerYear) {
            this.manufacturerYear = manufacturerYear;
            return this;
        }

        public LaptopSpecifications build() {
            return new LaptopSpecifications(this);
        }
    }
}
