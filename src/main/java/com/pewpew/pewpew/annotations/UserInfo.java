package com.pewpew.pewpew.annotations;

import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.jersey.message.filtering.EntityFiltering;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EntityFiltering
public @interface UserInfo {

    @SuppressWarnings("ClassExplicitlyAnnotation")
    public static final class Factory extends AnnotationLiteral<UserInfo> implements UserInfo {

        private Factory() {
        }

        public static UserInfo getInstance() {
            return new Factory();
        }

    }

}