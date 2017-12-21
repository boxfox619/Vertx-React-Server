package com.boxfox.support.vertx.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.vertx.core.http.HttpMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RouteRegistration {

    String uri();

    HttpMethod[] method() default {HttpMethod.GET};

    String description() default "";

    String paramDefaultType() default "";

}