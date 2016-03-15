package com.pewpew.pewpew.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.glassfish.jersey.message.filtering.EntityFiltering;

import org.glassfish.hk2.api.AnnotationLiteral;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EntityFiltering
public @interface UserInfo {

    @SuppressWarnings("ClassExplicitlyAnnotation")
    final class Factory extends AnnotationLiteral<UserInfo> implements UserInfo {

        private Factory() {
        }

        public static UserInfo getInstance() {
            return new Factory();
        }

    }

}