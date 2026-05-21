package ru.dgmu.smartqueue.validators.phone;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    
    private String defaultRegion;
    
    @Override
    public void initialize(Phone constraintAnnotation) {
        this.defaultRegion = constraintAnnotation.defaultRegion();
    }
    
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        
        try {
            PhoneNumber number = phoneUtil.parse(phone, defaultRegion);
            
            if (!phoneUtil.isValidNumber(number)) {
                addConstraintViolation(context);
                return false;
            }
            
            return true;
            
        } catch (NumberParseException e) {
            addConstraintViolation(context);
            return false;
        }
    }
    
    private void addConstraintViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Неверный формат номера телефона")
               .addConstraintViolation();
    }
}