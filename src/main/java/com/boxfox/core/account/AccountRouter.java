package com.boxfox.core.account;

import com.boxfox.core.account.login.LoginDTO;
import com.boxfox.core.account.login.LoginPerformer;
import com.boxfox.core.account.register.RegistPerformer;
import com.boxfox.support.data.Config;
import com.boxfox.support.data.Database;
import com.boxfox.support.vertx.middleware.JWTHandler;
import com.boxfox.support.vertx.router.Param;
import com.boxfox.support.vertx.router.RouteRegistration;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRouter {

    private LoginPerformer loginDAO;
    private RegistPerformer registerDAO;

    public AccountRouter() {
        loginDAO = new LoginPerformer();
        registerDAO = new RegistPerformer();
    }

    @RouteRegistration(uri = "/account/login", method = HttpMethod.POST)
    public void login(RoutingContext ctx, @Param String email, @Param String password) {
        LoginDTO result = loginDAO.login(email, password);
        if(result != null) {
            ctx.response().setStatusCode(HttpResponseStatus.OK.code());
            ctx.addCookie(Cookie.cookie(JWTHandler.COOKIE_NAME, createJWT(result.getUID(), result.getJTI())));
        }else{
            ctx.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code());
        }

        ctx.response().end();
    }

    @RouteRegistration(uri = "/account/register", method = HttpMethod.POST)
    public void register(RoutingContext ctx, @Param String email, @Param String password, @Param String nickname) {
        boolean result = registerDAO.register(email, password, nickname);
        ctx.response().setStatusCode(result ? HttpResponseStatus.OK.code() : HttpResponseStatus.PRECONDITION_FAILED.code());
        ctx.response().end();
    }

    private String createJWT(String uid, String jti) {
        String jwtString = null;
        try {
            JWSSigner signer = new MACSigner(Config.getDefaultInstance().getString("JWT-Secret"));
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .claim("iss", "ccshow.co.kr")
                    .claim("jti", jti)
                    .claim("uid", uid)
                    .claim("admin", false)
                    .build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);
            jwtString = signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return jwtString;
    }
}
