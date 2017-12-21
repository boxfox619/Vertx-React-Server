package com.boxfox.support.vertx.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
    String TYPE_QUERY = "TYPE_QUERY";
    String TYPE_PATH = "TYPE_PATH";
    String TYPE_BODY = "TYPE_BODY";
    String TYPE_AUTO = "TYPE_AUTO";

    String type() default TYPE_AUTO;

}