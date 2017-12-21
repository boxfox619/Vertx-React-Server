package com.boxfox.support.vertx.middleware;

import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;

public class CORSHandlerImpl implements CORSHandler {
    private static final String host = "ccshow.co.kr";

    @Override
    public void handle(RoutingContext ctx) {
        ctx.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Cookie, Origin, X-Requested-With, Content-Type");
        ctx.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, PATCH, GET, DELETE, OPTIONS, HEAD, CONNECT");
        ctx.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://" + host + "/*");
        ctx.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://" + host);
        ctx.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        ctx.next();
    }
}