package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Annotation {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public  @interface Operation{
        String value() default "";
    }
}
