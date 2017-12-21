package com.boxfox.core.account.login;

import com.boxfox.support.data.AbstractDTO;

import java.sql.ResultSet;

public class LoginDTO extends AbstractDTO {
    private String uid, jti;

    public LoginDTO(String uid, String jti) {
        this.uid = uid;
        this.jti = jti;
    }

    public String getUID() {
        return uid;
    }

    public String getJTI() {
        return jti;
    }
}
