package tdtu.edu.vn.model.specifications;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("ALL")
@NoArgsConstructor
@Setter
@Getter
@Entity
@DiscriminatorValue("PHONE_CASE")
public class PhoneCaseSpecifications extends Specifications{
    // use Prototype pattern
    @Override
    public PhoneCaseSpecifications clone(){
        try {
            return (PhoneCaseSpecifications) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
