package com.pewpew.pewpew.annotations;

import com.pewpew.pewpew.model.User;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;


@Target({TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidForModification.Validator.class)
public @interface ValidForModification {

    String message() default "{constraint.ValidForModification}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidForModification, User> {

        @Override
        public void initialize(ValidForModification validForCreation) {}

        @Override
        public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
            return user != null
                    && (user.getPassword() != null || user.getEmail() != null || user.getLogin() != null);
        }
    }

}