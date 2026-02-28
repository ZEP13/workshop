package workshop.zepcla.customAnotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import workshop.zepcla.utils.ValidPhoneValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPhoneValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {

    String message() default "Invalid phone number";

    String[] countries() default { "BE", "FR" };

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
