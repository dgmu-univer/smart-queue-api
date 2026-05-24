package ru.dgmu.smartqueue.validators.phone;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {

  String message() default "Неверный формат номера телефона";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String defaultRegion() default "RU";
}