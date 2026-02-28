package workshop.zepcla.utils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.NumberParseException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import workshop.zepcla.customAnotations.ValidPhone;

// libphone permet de valide et verifier si un numero existe ou pas, et de le formater dans un format international
// mieux qu'un simple regex qui ne verifie que la structure du numero, mais pas si il existe ou pas donc ou pourrai mettre +32 11111111 et ca passerai 
// alors que ce numero n'existe pas, avec libphone on pourra verifier que ce numero n'existe pas et donc le refuser

public class ValidPhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private String[] countries;

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        this.countries = constraintAnnotation.countries();
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {

        if (phone == null || phone.isBlank()) {
            return false;
        }

        for (String country : countries) {
            try {
                var number = phoneUtil.parse(phone, country);

                if (phoneUtil.isValidNumberForRegion(number, country)) {
                    return true;
                }

            } catch (NumberParseException ignored) {
            }
        }

        return false;
    }
}
