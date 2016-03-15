package com.pewpew.pewpew.model;

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
@Constraint(validatedBy = ValidForCreation.Validator.class)
public @interface ValidForCreation {

    String message() default "{pl.pjagielski.constraint.ValidForCreation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidForCreation, User> {

        @Override
        public void initialize(ValidForCreation validForCreation) {}

        @Override
        public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
            return user != null
                    && user.getLogin() == null
                    && user.getPassword() != null
                    && user.getLogin() != null;
        }
    }

}



