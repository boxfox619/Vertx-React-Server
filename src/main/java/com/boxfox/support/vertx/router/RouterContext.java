package com.boxfox.support.vertx.router;

import io.vertx.core.http.HttpMethod;

public class RouterContext implements Comparable<RouterContext> {
    private String uri;
    private HttpMethod[] methods;
    private Object routingInstance;

    public RouterContext(RouteRegistration routeRegistration, Object instance) {
        this.uri = routeRegistration.uri();
        this.methods = routeRegistration.method();
        this.routingInstance = instance;
    }

    public Object getInstance() {
        return routingInstance;
    }

    public boolean instanceOf(Class<?> clazz) {
        return routingInstance.getClass().equals(clazz);
    }

    @Override
    public int compareTo(RouterContext targetCtx) {
        return uri.compareTo(targetCtx.uri);
    }
}
