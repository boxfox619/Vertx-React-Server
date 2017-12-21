package com.boxfox.support.vertx.middleware;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface JWTHandler extends Handler<RoutingContext> {
    String COOKIE_NAME = "Auth-Token";

    static JWTHandler create() {
        return new JWTHandlerImpl();
    }

}