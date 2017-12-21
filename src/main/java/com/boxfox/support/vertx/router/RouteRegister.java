package com.boxfox.support.vertx.router;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.net.HttpHeaders;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.reflections.Reflections;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/* boxfox 2017.02.13*/

public class RouteRegister {
    private List<RouterContext> routerList;
    private Router router;

    public static RouteRegister routing(Vertx vertx) {
        return new RouteRegister(Router.router(vertx));
    }

    private RouteRegister(Router router) {
        this.router = router;
        this.routerList = new ArrayList();
    }

    public void route(Handler<RoutingContext> handler) {
        router.route().handler(handler);
    }

    public void route(String... pacakge) {
        Reflections routerAnnotations = new Reflections(pacakge);
        Set<Class<?>> annotatedClass = routerAnnotations.getTypesAnnotatedWith(RouteRegistration.class);
        Set<Method> annotatedMethod = routerAnnotations.getMethodsAnnotatedWith(RouteRegistration.class);

        annotatedClass.forEach(c -> {
            RouteRegistration annotation = c.getAnnotation(RouteRegistration.class);
            try {
                Object routingInstance = c.newInstance();
                Handler handler = (Handler<RoutingContext>) routingInstance;
                for (HttpMethod method : annotation.method())
                    router.route(method, annotation.uri()).handler(handler);
                routerList.add(new RouterContext(annotation, routingInstance));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        annotatedMethod.forEach(m -> {
            RouteRegistration annotation = m.getAnnotation(RouteRegistration.class);
            try {
                Object instance = searchCreatedInstance(m.getDeclaringClass());
                if (instance == null)
                    instance = m.getDeclaringClass().newInstance();
                Handler handler = createMethodHandler(instance, m);
                for (HttpMethod method : annotation.method())
                    router.route(method, annotation.uri()).handler(handler);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private Handler<RoutingContext> createMethodHandler(Object instance, Method m) {
        return ctx -> {
            List<Object> argments = new ArrayList<>();
            Arrays.stream(m.getParameters()).forEach(param -> {
                String paramType = getParamType(param, m);
                String paramName = param.getName();
                Class<?> paramClass = param.getType();
                if (paramClass.equals(Handler.class)) {
                    argments.add(ctx);
                } else {
                    Object paramData = null;
                    switch (paramType) {
                        case Param.TYPE_BODY:
                            paramData = getParameterFromBody(ctx, paramName, paramClass);
                            break;
                        case Param.TYPE_PATH:
                            paramData = castingParameter(ctx.pathParam(paramName), paramType);
                            break;
                        default:
                            paramData = castingParameter(ctx.queryParam(paramName).get(0), paramType);
                    }
                    argments.add(paramData);
                }
            });
            try {
                m.invoke(instance, argments.toArray());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        };
    }

    private String getParamType(Parameter param, Method m) {
        String defaultParamType = m.getAnnotation(RouteRegistration.class).paramDefaultType();
        String paramType = param.getAnnotation(Param.class).type();
        if (param.equals(Param.TYPE_AUTO)) {
            if (defaultParamType.length() == 0) {
                HttpMethod method = m.getAnnotation(RouteRegistration.class).method()[0];
                paramType = (method == HttpMethod.GET) ? Param.TYPE_QUERY : Param.TYPE_BODY;
            } else {
                paramType = defaultParamType;
            }
        }
        return paramType;
    }

    private Object castingParameter(String str, String paramType) {
        Object paramData = str;
        if (paramType.equals(Integer.class)) {
            paramData = Integer.valueOf(str);
        } else if (paramType.equals(Boolean.class)) {
            paramData = Boolean.valueOf(str);
        } else if (paramType.equals(Double.class)) {
            paramData = Double.valueOf(str);
        } else if (paramType.equals(Float.class)) {
            paramData = Float.valueOf(str);
        } else if (paramData.equals(JsonObject.class)) {
            paramData = new JsonObject(str);
        } else if (paramData.equals(JsonArray.class)) {
            paramData = new JsonArray(str);
        }
        return paramData;
    }

    private Object getParameterFromBody(RoutingContext ctx, String paramName, Class<?> paramType) {
        Object paramData = null;
        boolean json = ctx.request().getHeader(HttpHeaders.CONTENT_TYPE).toLowerCase().contains("json");
        JsonObject bodyData = ctx.getBodyAsJson();
        if (paramType.equals(String.class)) {
            paramData = bodyData.getString(paramName);
        } else if (paramType.equals(Integer.class)) {
            paramData = bodyData.getInteger(paramName);
        } else if (paramType.equals(Boolean.class)) {
            paramData = bodyData.getBoolean(paramName);
        } else if (paramType.equals(Double.class)) {
            paramData = bodyData.getDouble(paramName);
        } else if (paramType.equals(Float.class)) {
            paramData = bodyData.getFloat(paramName);
        } else if (paramData.equals(JsonObject.class)) {
            paramData = bodyData.getJsonObject(paramName);
        } else if (paramData.equals(JsonArray.class)) {
            paramData = bodyData.getJsonArray(paramName);
        } else if (paramData.equals(byte[].class)) {
            paramData = bodyData.getBinary(paramName);
        }
        return paramData;
    }

    private Object searchCreatedInstance(Class<?> clazz) {
        for (RouterContext ctx : this.routerList) {
            if (ctx.instanceOf(clazz))
                return ctx.getInstance();
        }
        return null;
    }

    public Router getRouter() {
        return router;
    }
}