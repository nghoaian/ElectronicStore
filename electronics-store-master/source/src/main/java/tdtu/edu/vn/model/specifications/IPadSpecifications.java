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
@DiscriminatorValue("IPAD")
public class IPadSpecifications extends Specifications{
    private String screen;
    private String rearCamera;
    private String frontCamera;
    private String RAM;
    private String internalProcessor;
    private String CPU;
    private String GPU;
    private String origin;
    private String releaseDate;

    private IPadSpecifications(Builder builder){
        this.screen = builder.screen;
        this.rearCamera = builder.rearCamera;
        this.frontCamera = builder.frontCamera;
        this.RAM = builder.RAM;
        this.internalProcessor = builder.internalProcessor;
        this.CPU = builder.CPU;
        this.GPU = builder.GPU;
        this.origin = builder.origin;
        this.releaseDate = builder.releaseDate;
    }

    // use Prototype pattern
    @Override
    public IPadSpecifications clone(){
        try {
            return (IPadSpecifications) super.clone();
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
        private String origin;
        private String releaseDate;

        public Builder screen(String screen) {
            this.screen = screen;
            return this;
        }

        public Builder rearCamera(String rearCamera) {
            this.rearCamera = rearCamera;
            return this;
        }

        public Builder frontCamera(String frontCamera) {
            this.frontCamera = frontCamera;
            return this;
        }

        public Builder RAM(String RAM) {
            this.RAM = RAM;
            return this;
        }

        public Builder internalProcessor(String internalProcessor) {
            this.internalProcessor = internalProcessor;
            return this;
        }

        public Builder CPU(String CPU) {
            this.CPU = CPU;
            return this;
        }

        public Builder GPU(String GPU) {
            this.GPU = GPU;
            return this;
        }

        public Builder origin(String origin) {
            this.origin = origin;
            return this;
        }

        public Builder releaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public IPadSpecifications build() {
            return new IPadSpecifications(this);
        }
    }
}
