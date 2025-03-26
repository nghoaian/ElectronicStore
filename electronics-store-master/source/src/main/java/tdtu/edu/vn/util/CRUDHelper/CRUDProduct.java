package tdtu.edu.vn.util.CRUDHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tdtu.edu.vn.model.Product;
import tdtu.edu.vn.model.specifications.*;
import tdtu.edu.vn.payload.ResponseData;
import tdtu.edu.vn.service.ProductService;
import tdtu.edu.vn.service.SpecificationsService;

import java.util.ArrayList;
import java.util.List;

// create adapter for CRUD operations on Product with general interfaces
@Service
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class CRUDProduct {
    @Autowired
    private ProductService productService;
    @Autowired
    private SpecificationsService specificationsService;

    public ResponseEntity<?> create(ProductSpecification entity) {
        try {
            if (entity.product == null || entity.type == null || entity.specifications == null) {
                return ResponseEntity.badRequest().body("Create product failed. Please provide product, type and specifications");
            }

            Product product = entity.product;
            String type = entity.type;
            Specifications specifications = getSpecifications(type, entity.specifications);


            product.setSpecifications(specifications);

            specificationsService.save(specifications);
            Product result = productService.save(product);

            ResponseData responseData = new ResponseData.Builder()
                    .data(result)
                    .message("Create product successfully")
                    .build();

            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Create product failed");
        }
    }

    public ResponseEntity<?> update(ProductSpecification entity){
        try {
            if (entity.product == null || (entity.type != null && entity.specifications == null)) {
                return ResponseEntity.badRequest().body("Update product failed. Please provide product, type and specifications");
            }

            if (entity.product.getId() == 0) {
                return ResponseEntity.badRequest().body("Update product failed. Please provide product id");
            }
            Product product = productService.findById(entity.product.getId());
            Specifications specifications = product.getSpecifications();

            Product p = entity.product;
            if (p.getName() != null) product.setName(p.getName());
            if (p.getImages() != null) product.setImages(p.getImages());
            if (p.getPrice() != 0) product.setPrice(p.getPrice());
            if (p.getCategory() != null) product.setCategory(p.getCategory());
            if (p.getBrand() != null) product.setBrand(p.getBrand());
            if (p.getEvaluationPoint() != 0) product.setEvaluationPoint(p.getEvaluationPoint());

            String type = entity.type;
            Specifications spec = getUpdateSpecifications(type, (Specifications) entity.specifications, specifications);

            product.setSpecifications(specifications);

            ResponseData responseData = new ResponseData.Builder()
                    .data(productService.update(product))
                    .message("Update product successfully")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update product failed");
        }
    }

    public ResponseEntity<?> delete(ProductSpecification entity){
        try {
            if (entity.product == null) {
                return ResponseEntity.badRequest().body("Delete product failed. Please provide product");
            }

            Product product = productService.findById(entity.product.getId());
            if(product == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }

            productService.delete(product);
            specificationsService.delete(product.getSpecifications());

            ResponseData responseData = new ResponseData.Builder()
                    .data(product)
                    .message("Delete product successfully")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Delete product failed");
        }
    }

    public ResponseEntity<?> show(ProductSpecification entity){
        try {
            if (entity.product == null) {
                return ResponseEntity.badRequest().body("Show product failed. Please provide product");
            }

            Product product = productService.findById(entity.product.getId());
            if(product == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }

            String type = product.getSpecifications().getSpecificationsType();
            Object specifications = product.getSpecifications();

            ProductSpecification productSpecification = new ProductSpecification.Builder()
                    .setProduct(product)
                    .setType(type)
                    .setSpecifications(specifications)
                    .build();

            ResponseData responseData = new ResponseData.Builder()
                    .data(productSpecification)
                    .message("Show product successfully")
                    .build();

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Show product failed");
        }
    }

    // only search for id or specifications
    public ResponseEntity<?> search(ProductSpecification entity) {
        try {
            Product product = entity.product;
            String type = entity.type;
            Specifications specifications = getSpecifications(type, entity.specifications);

            List<ProductSpecification> result = filterProduct(productService.findAll(), product);
            result = filterSpecifications(result, type, specifications);

            if(result.isEmpty()){
                return ResponseEntity.ok("Product not found");
            }

            ResponseData responseData = new ResponseData.Builder()
                    .data(result)
                    .message("Search product successfully")
                    .build();

            return ResponseEntity.ok(responseData);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Search product failed");
        }
    }

    private List<ProductSpecification> filterProduct(List<Product> list, Product product) {
        if(product == null){
            product = new Product();
        }

        List<ProductSpecification> result = new ArrayList<>();

        Product finalProduct = product;
        list.stream().filter(p -> (finalProduct.getId() == 0 || p.getId() == finalProduct.getId()) &&
                (finalProduct.getName() == null || p.getName().contains(finalProduct.getName())) &&
                (finalProduct.getCategory() == null || p.getCategory().contains(finalProduct.getCategory())) &&
                (finalProduct.getBrand() == null || p.getBrand().contains(finalProduct.getBrand())) &&
                ( finalProduct.getPrice() == 0 ||p.getPrice() <= finalProduct.getPrice()) &&
                (finalProduct.getEvaluationPoint() == 0 || p.getEvaluationPoint() == finalProduct.getEvaluationPoint()))
                .forEach(p -> result.add(new ProductSpecification.Builder()
                        .setProduct(p)
                        .setType(p.getSpecifications().getSpecificationsType())
                        .setSpecifications(p.getSpecifications())
                        .build()));

        return result;
    }
    private List<ProductSpecification> filterSpecifications(List<ProductSpecification> list, String type, Specifications specifications) {
        if(!type.isEmpty()) {
            list = list.stream().filter(p -> p.getType().equals(type)).toList();
            // for filter specifications
            switch (type) {
                case "ChargerSpecifications" -> {
                    ChargerSpecifications chargerSpecifications = (ChargerSpecifications) specifications;
                    list = list.stream().filter(p -> {
                        ChargerSpecifications charger = (ChargerSpecifications) p.getSpecifications();
                        return (chargerSpecifications.getType() == null || charger.getType().contains(chargerSpecifications.getType())) &&
                                (chargerSpecifications.getInterfacePort() == null || charger.getInterfacePort().contains(chargerSpecifications.getInterfacePort())) &&
                                (chargerSpecifications.getFeatures() == null || charger.getFeatures().contains(chargerSpecifications.getFeatures())) &&
                                (chargerSpecifications.getCompatibility() == null || charger.getCompatibility().contains(chargerSpecifications.getCompatibility()));
                    }).toList();
                }
                case "HeadphoneSpecifications" -> {
                    HeadphoneSpecifications headphoneSpecifications = (HeadphoneSpecifications) specifications;
                    list = list.stream().filter(p -> {
                        HeadphoneSpecifications headphone = (HeadphoneSpecifications) p.getSpecifications();
                        return (headphoneSpecifications.getType() == null || headphone.getType().contains(headphoneSpecifications.getType())) &&
                                (headphoneSpecifications.getConnectivity() == null || headphone.getConnectivity().contains(headphoneSpecifications.getConnectivity())) &&
                                (headphoneSpecifications.getOrigin() == null || headphone.getOrigin().contains(headphoneSpecifications.getOrigin())) &&
                                (headphoneSpecifications.getWarrantyPeriod() == null || headphone.getWarrantyPeriod().contains(headphoneSpecifications.getWarrantyPeriod()));
                    }).toList();
                }
                case "IPadSpecifications" -> {
                    IPadSpecifications iPadSpecifications = (IPadSpecifications) specifications;
                    list = list.stream().filter(p -> {
                        IPadSpecifications iPad = (IPadSpecifications) p.getSpecifications();
                        return (iPadSpecifications.getScreen() == null || iPad.getScreen().contains(iPadSpecifications.getScreen())) &&
                                (iPadSpecifications.getRearCamera() == null || iPad.getRearCamera().contains(iPadSpecifications.getRearCamera())) &&
                                (iPadSpecifications.getFrontCamera() == null || iPad.getFrontCamera().contains(iPadSpecifications.getFrontCamera())) &&
                                (iPadSpecifications.getRAM() == null || iPad.getRAM().contains(iPadSpecifications.getRAM())) &&
                                (iPadSpecifications.getInternalProcessor() == null || iPad.getInternalProcessor().contains(iPadSpecifications.getInternalProcessor())) &&
                                (iPadSpecifications.getCPU() == null || iPad.getCPU().contains(iPadSpecifications.getCPU())) &&
                                (iPadSpecifications.getGPU() == null || iPad.getGPU().contains(iPadSpecifications.getGPU())) &&
                                (iPadSpecifications.getOrigin() == null || iPad.getOrigin().contains(iPadSpecifications.getOrigin())) &&
                                (iPadSpecifications.getReleaseDate() == null || iPad.getReleaseDate().contains(iPadSpecifications.getReleaseDate()));
                    }).toList();
                }
                case "KeyboardSpecifications" -> {
                    KeyboardSpecifications keyboardSpecifications = (KeyboardSpecifications) specifications;
                    list = list.stream().filter(p -> {
                        KeyboardSpecifications keyboard = (KeyboardSpecifications) p.getSpecifications();
                        return (keyboardSpecifications.getType() == null || keyboard.getType().contains(keyboardSpecifications.getType())) &&
                                (keyboardSpecifications.getConnectivity() == null || keyboard.getConnectivity().contains(keyboardSpecifications.getConnectivity())) &&
                                (keyboardSpecifications.getInterfacePort() == null || keyboard.getInterfacePort().contains(keyboardSpecifications.getInterfacePort())) &&
                                (keyboardSpecifications.getFeatures() == null || keyboard.getFeatures().contains(keyboardSpecifications.getFeatures())) &&
                                (keyboardSpecifications.getOperatingSystem() == null || keyboard.getOperatingSystem().contains(keyboardSpecifications.getOperatingSystem())) &&
                                (keyboardSpecifications.getOrigin() == null || keyboard.getOrigin().contains(keyboardSpecifications.getOrigin())) &&
                                (keyboardSpecifications.getWarrantyPeriod() == null || keyboard.getWarrantyPeriod().contains(keyboardSpecifications.getWarrantyPeriod()));
                    }).toList();
                }
                case "LaptopSpecifications" -> {
                    LaptopSpecifications laptopSpecifications = (LaptopSpecifications) specifications;
                    list = list.stream().filter(p -> {
                        LaptopSpecifications laptop = (LaptopSpecifications) p.getSpecifications();
                        return (laptopSpecifications.getScreen() == null || laptop.getScreen().contains(laptopSpecifications.getScreen())) &&
                                (laptopSpecifications.getRAM() == null || laptop.getRAM().contains(laptopSpecifications.getRAM())) &&
                                (laptopSpecifications.getCPU() == null || laptop.getCPU().contains(laptopSpecifications.getCPU())) &&
                                (laptopSpecifications.getHardDrive() == null || laptop.getHardDrive().contains(laptopSpecifications.getHardDrive())) &&
                                (laptopSpecifications.getGraphics() == null || laptop.getGraphics().contains(laptopSpecifications.getGraphics())) &&
                                (laptopSpecifications.getWeight() == null || laptop.getWeight().contains(laptopSpecifications.getWeight())) &&
                                (laptopSpecifications.getDimensions() == null || laptop.getDimensions().contains(laptopSpecifications.getDimensions())) &&
                                (laptopSpecifications.getOrigin() == null || laptop.getOrigin().contains(laptopSpecifications.getOrigin())) &&
                                (laptopSpecifications.getManufacturerYear() == null || laptop.getManufacturerYear().contains(laptopSpecifications.getManufacturerYear()));
                    }).toList();
                }
                case "MouseSpecifications" -> {
                    MouseSpecifications mouseSpecifications = (MouseSpecifications) specifications;
                    list = list.stream().filter(p -> {
                        MouseSpecifications mouse = (MouseSpecifications) p.getSpecifications();
                        return (mouseSpecifications.getType() == null || mouse.getType().contains(mouseSpecifications.getType())) &&
                                (mouseSpecifications.getConnectivity() == null || mouse.getConnectivity().contains(mouseSpecifications.getConnectivity())) &&
                                (mouseSpecifications.getOrigin() == null || mouse.getOrigin().contains(mouseSpecifications.getOrigin())) &&
                                (mouseSpecifications.getWarrantyPeriod() == null || mouse.getWarrantyPeriod().contains(mouseSpecifications.getWarrantyPeriod()));
                    }).toList();
                }
                case "PhoneCaseSpecifications" -> {
                    // don't have any specifications
                }
                case "PhoneSpecifications" -> {
                    PhoneSpecifications phoneSpecifications = (PhoneSpecifications) specifications;
                    list = list.stream().filter(p -> {
                        PhoneSpecifications phone = (PhoneSpecifications) p.getSpecifications();
                        return (phoneSpecifications.getScreen() == null || phone.getScreen().contains(phoneSpecifications.getScreen())) &&
                                (phoneSpecifications.getRearCamera() == null || phone.getRearCamera().contains(phoneSpecifications.getRearCamera())) &&
                                (phoneSpecifications.getFrontCamera() == null || phone.getFrontCamera().contains(phoneSpecifications.getFrontCamera())) &&
                                (phoneSpecifications.getRAM() == null || phone.getRAM().contains(phoneSpecifications.getRAM())) &&
                                (phoneSpecifications.getInternalProcessor() == null || phone.getInternalProcessor().contains(phoneSpecifications.getInternalProcessor())) &&
                                (phoneSpecifications.getCPU() == null || phone.getCPU().contains(phoneSpecifications.getCPU())) &&
                                (phoneSpecifications.getGPU() == null || phone.getGPU().contains(phoneSpecifications.getGPU())) &&
                                (phoneSpecifications.getBatteryCapacity() == null || phone.getBatteryCapacity().contains(phoneSpecifications.getBatteryCapacity())) &&
                                (phoneSpecifications.getSimCard() == null || phone.getSimCard().contains(phoneSpecifications.getSimCard())) &&
                                (phoneSpecifications.getOperatingSystem() == null || phone.getOperatingSystem().contains(phoneSpecifications.getOperatingSystem())) &&
                                (phoneSpecifications.getOrigin() == null || phone.getOrigin().contains(phoneSpecifications.getOrigin())) &&
                                (phoneSpecifications.getReleaseDate() == null || phone.getReleaseDate().contains(phoneSpecifications.getReleaseDate()));
                    }).toList();
                }
                case "SmartWatchSpecifications" -> {
                    SmartWatchSpecifications smartWatchSpecifications = (SmartWatchSpecifications) specifications;
                    list = list.stream().filter(p -> {
                        SmartWatchSpecifications smartWatch = (SmartWatchSpecifications) p.getSpecifications();
                        return (smartWatchSpecifications.getScreen() == null || smartWatch.getScreen().contains(smartWatchSpecifications.getScreen())) &&
                                (smartWatchSpecifications.getInternalProcessor() == null || smartWatch.getInternalProcessor().contains(smartWatchSpecifications.getInternalProcessor())) &&
                                (smartWatchSpecifications.getMaterial() == null || smartWatch.getMaterial().contains(smartWatchSpecifications.getMaterial())) &&
                                (smartWatchSpecifications.getOperatingSystem() == null || smartWatch.getOperatingSystem().contains(smartWatchSpecifications.getOperatingSystem())) &&
                                (smartWatchSpecifications.getBatteryCapacity() == null || smartWatch.getBatteryCapacity().contains(smartWatchSpecifications.getBatteryCapacity())) &&
                                (smartWatchSpecifications.getWarrantyPeriod() == null || smartWatch.getWarrantyPeriod().contains(smartWatchSpecifications.getWarrantyPeriod()));
                    }).toList();
                }
            }
        }
        return list;
    }
    private Specifications getSpecifications(String type, Object specifications) {
        ObjectMapper mapper = new ObjectMapper();
        return switch (type) {
            case "CHARGER" -> mapper.convertValue(specifications, ChargerSpecifications.class);
            case "HEADPHONE" -> mapper.convertValue(specifications, HeadphoneSpecifications.class);
            case "IPAD" -> mapper.convertValue(specifications, IPadSpecifications.class);
            case "KEYBOARD" -> mapper.convertValue(specifications, KeyboardSpecifications.class);
            case "LAPTOP" -> mapper.convertValue(specifications, LaptopSpecifications.class);
            case "MOUSE" -> mapper.convertValue(specifications, MouseSpecifications.class);
            case "PHONE_CASE" -> mapper.convertValue(specifications, PhoneCaseSpecifications.class);
            case "PHONE" -> mapper.convertValue(specifications, PhoneSpecifications.class);
            case "SMART_WATCH" -> mapper.convertValue(specifications, SmartWatchSpecifications.class);
            default -> mapper.convertValue(specifications, Specifications.class);
        };
    }

    private Specifications getUpdateSpecifications(String type, Specifications entity, Specifications specifications) {
        switch (type){
            case "ChargeSpecifications" -> {
                ChargerSpecifications chargerSpecifications = (ChargerSpecifications) entity;
                ChargerSpecifications charger = (ChargerSpecifications) specifications;
                if(chargerSpecifications.getType() != null) charger.setType(chargerSpecifications.getType());
                if(chargerSpecifications.getInterfacePort() != null) charger.setInterfacePort(chargerSpecifications.getInterfacePort());
                if(chargerSpecifications.getFeatures() != null) charger.setFeatures(chargerSpecifications.getFeatures());
                if(chargerSpecifications.getCompatibility() != null) charger.setCompatibility(chargerSpecifications.getCompatibility());
                return charger;
            }
            case "HeadphoneSpecifications" -> {
                HeadphoneSpecifications headphoneSpecifications = (HeadphoneSpecifications) entity;
                HeadphoneSpecifications headphone = (HeadphoneSpecifications) specifications;
                if(headphoneSpecifications.getType() != null) headphone.setType(headphoneSpecifications.getType());
                if(headphoneSpecifications.getConnectivity() != null) headphone.setConnectivity(headphoneSpecifications.getConnectivity());
                if(headphoneSpecifications.getOrigin() != null) headphone.setOrigin(headphoneSpecifications.getOrigin());
                if(headphoneSpecifications.getWarrantyPeriod() != null) headphone.setWarrantyPeriod(headphoneSpecifications.getWarrantyPeriod());
                return headphone;
            }
            case "IPadSpecifications" -> {
                IPadSpecifications iPadSpecifications = (IPadSpecifications) entity;
                IPadSpecifications iPad = (IPadSpecifications) specifications;
                if(iPadSpecifications.getScreen() != null) iPad.setScreen(iPadSpecifications.getScreen());
                if(iPadSpecifications.getRearCamera() != null) iPad.setRearCamera(iPadSpecifications.getRearCamera());
                if(iPadSpecifications.getFrontCamera() != null) iPad.setFrontCamera(iPadSpecifications.getFrontCamera());
                if(iPadSpecifications.getRAM() != null) iPad.setRAM(iPadSpecifications.getRAM());
                if(iPadSpecifications.getInternalProcessor() != null) iPad.setInternalProcessor(iPadSpecifications.getInternalProcessor());
                if(iPadSpecifications.getCPU() != null) iPad.setCPU(iPadSpecifications.getCPU());
                if(iPadSpecifications.getGPU() != null) iPad.setGPU(iPadSpecifications.getGPU());
                if(iPadSpecifications.getOrigin() != null) iPad.setOrigin(iPadSpecifications.getOrigin());
                if(iPadSpecifications.getReleaseDate() != null) iPad.setReleaseDate(iPadSpecifications.getReleaseDate());
                return iPad;
            }
            case "KeyboardSpecifications" -> {
                KeyboardSpecifications keyboardSpecifications = (KeyboardSpecifications) entity;
                KeyboardSpecifications keyboard = (KeyboardSpecifications) specifications;
                if(keyboardSpecifications.getType() != null) keyboard.setType(keyboardSpecifications.getType());
                if(keyboardSpecifications.getConnectivity() != null) keyboard.setConnectivity(keyboardSpecifications.getConnectivity());
                if(keyboardSpecifications.getInterfacePort() != null) keyboard.setInterfacePort(keyboardSpecifications.getInterfacePort());
                if(keyboardSpecifications.getFeatures() != null) keyboard.setFeatures(keyboardSpecifications.getFeatures());
                if(keyboardSpecifications.getOperatingSystem() != null) keyboard.setOperatingSystem(keyboardSpecifications.getOperatingSystem());
                if(keyboardSpecifications.getOrigin() != null) keyboard.setOrigin(keyboardSpecifications.getOrigin());
                if(keyboardSpecifications.getWarrantyPeriod() != null) keyboard.setWarrantyPeriod(keyboardSpecifications.getWarrantyPeriod());
                return keyboard;
            }
            case "LaptopSpecifications" -> {
                LaptopSpecifications laptopSpecifications = (LaptopSpecifications) entity;
                LaptopSpecifications laptop = (LaptopSpecifications) specifications;
                if(laptopSpecifications.getScreen() != null) laptop.setScreen(laptopSpecifications.getScreen());
                if(laptopSpecifications.getRAM() != null) laptop.setRAM(laptopSpecifications.getRAM());
                if(laptopSpecifications.getCPU() != null) laptop.setCPU(laptopSpecifications.getCPU());
                if(laptopSpecifications.getHardDrive() != null) laptop.setHardDrive(laptopSpecifications.getHardDrive());
                if(laptopSpecifications.getGraphics() != null) laptop.setGraphics(laptopSpecifications.getGraphics());
                if(laptopSpecifications.getWeight() != null) laptop.setWeight(laptopSpecifications.getWeight());
                if(laptopSpecifications.getDimensions() != null) laptop.setDimensions(laptopSpecifications.getDimensions());
                if(laptopSpecifications.getOrigin() != null) laptop.setOrigin(laptopSpecifications.getOrigin());
                if(laptopSpecifications.getManufacturerYear() != null) laptop.setManufacturerYear(laptopSpecifications.getManufacturerYear());
                return laptop;
            }
            case "MouseSpecifications" -> {
                MouseSpecifications mouseSpecifications = (MouseSpecifications) entity;
                MouseSpecifications mouse = (MouseSpecifications) specifications;
                if(mouseSpecifications.getType() != null) mouse.setType(mouseSpecifications.getType());
                if(mouseSpecifications.getConnectivity() != null) mouse.setConnectivity(mouseSpecifications.getConnectivity());
                if(mouseSpecifications.getOrigin() != null) mouse.setOrigin(mouseSpecifications.getOrigin());
                if(mouseSpecifications.getWarrantyPeriod() != null) mouse.setWarrantyPeriod(mouseSpecifications.getWarrantyPeriod());
                return mouse;
            }
            case "PhoneCaseSpecifications" -> {
                // don't have any specifications
            }
            case "PhoneSpecifications" -> {
                PhoneSpecifications phoneSpecifications = (PhoneSpecifications) entity;
                PhoneSpecifications phone = (PhoneSpecifications) specifications;
                if(phoneSpecifications.getScreen() != null) phone.setScreen(phoneSpecifications.getScreen());
                if(phoneSpecifications.getRearCamera() != null) phone.setRearCamera(phoneSpecifications.getRearCamera());
                if(phoneSpecifications.getFrontCamera() != null) phone.setFrontCamera(phoneSpecifications.getFrontCamera());
                if(phoneSpecifications.getRAM() != null) phone.setRAM(phoneSpecifications.getRAM());
                if(phoneSpecifications.getInternalProcessor() != null) phone.setInternalProcessor(phoneSpecifications.getInternalProcessor());
                if(phoneSpecifications.getCPU() != null) phone.setCPU(phoneSpecifications.getCPU());
                if(phoneSpecifications.getGPU() != null) phone.setGPU(phoneSpecifications.getGPU());
                if(phoneSpecifications.getBatteryCapacity() != null) phone.setBatteryCapacity(phoneSpecifications.getBatteryCapacity());
                if(phoneSpecifications.getSimCard() != null) phone.setSimCard(phoneSpecifications.getSimCard());
                if(phoneSpecifications.getOperatingSystem() != null) phone.setOperatingSystem(phoneSpecifications.getOperatingSystem());
                if(phoneSpecifications.getOrigin() != null) phone.setOrigin(phoneSpecifications.getOrigin());
                if(phoneSpecifications.getReleaseDate() != null) phone.setReleaseDate(phoneSpecifications.getReleaseDate());
                return phone;
            }
            case "SmartWatchSpecifications" -> {
                SmartWatchSpecifications smartWatchSpecifications = (SmartWatchSpecifications) entity;
                SmartWatchSpecifications smartWatch = (SmartWatchSpecifications) specifications;
                if(smartWatchSpecifications.getScreen() != null) smartWatch.setScreen(smartWatchSpecifications.getScreen());
                if(smartWatchSpecifications.getInternalProcessor() != null) smartWatch.setInternalProcessor(smartWatchSpecifications.getInternalProcessor());
                if(smartWatchSpecifications.getMaterial() != null) smartWatch.setMaterial(smartWatchSpecifications.getMaterial());
                if(smartWatchSpecifications.getOperatingSystem() != null) smartWatch.setOperatingSystem(smartWatchSpecifications.getOperatingSystem());
                if(smartWatchSpecifications.getBatteryCapacity() != null) smartWatch.setBatteryCapacity(smartWatchSpecifications.getBatteryCapacity());
                if(smartWatchSpecifications.getWarrantyPeriod() != null) smartWatch.setWarrantyPeriod(smartWatchSpecifications.getWarrantyPeriod());
                return smartWatch;
            }
        }
        return specifications;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ProductSpecification {
        private Product product;
        private String type;
        private Object specifications;

        public ProductSpecification(Builder builder) {
            this.product = builder.product;
            this.type = builder.type;
            this.specifications = builder.specifications;
        }

        public static class Builder {
            private Product product;
            private String type;
            private Object specifications;

            public Builder setProduct(Product product) {
                this.product = product;
                return this;
            }

            public Builder setType(String type) {
                this.type = type;
                return this;
            }

            public Builder setSpecifications(Object specifications) {
                this.specifications = specifications;
                return this;
            }

            public ProductSpecification build() {
                return new ProductSpecification(this);
            }
        }
    }
}
