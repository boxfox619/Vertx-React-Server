package com.boxfox.support.vertx.middleware;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public interface CORSHandler extends Handler<RoutingContext> {

    static CORSHandler create() {
        return new CORSHandlerImpl();
    }
}