package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SpringBootApplication
@MapperScan("com.example.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /**
     * 定义一个Operation注解
     */
    //@Target 声明这是一个自定义注解类，ElementType.METHOD 表明此注解可声明在方法上
    //@Retention 声明注解保留期限，RetentionPolicy.RUNTIME 表明此注解可保留至运行时，可以通过反射获取注解信息
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public  @interface Operation{
        String value() default "";
    }
}
