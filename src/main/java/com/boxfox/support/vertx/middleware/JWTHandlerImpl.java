package com.boxfox.support.vertx.middleware;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.SignedJWT;
import io.vertx.ext.web.RoutingContext;

import java.text.ParseException;

public class JWTHandlerImpl implements JWTHandler {

    @Override
    public void handle(RoutingContext ctx) {
        String jwtString = ctx.getCookie(COOKIE_NAME).getValue();
        if (jwtString != null) {
            try {
                JWT signedJWT = (SignedJWT) SignedJWT.parse(jwtString);
                ctx.data().put("jti", signedJWT.getJWTClaimsSet().getClaim("jti"));
                ctx.data().put("uid", signedJWT.getJWTClaimsSet().getClaim("uid"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        ctx.next();
    }

}
