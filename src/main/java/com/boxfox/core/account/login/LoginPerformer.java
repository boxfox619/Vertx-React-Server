package com.boxfox.core.account.login;

import com.boxfox.support.data.AbstractDAO;
import com.boxfox.support.data.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPerformer extends AbstractDAO {

    public LoginDTO login(String email, String password) {
        LoginDTO loginDTO = null;
        String loginQuery = Database.getQueryFromResource("login.sql");
        try {
            ResultSet rs = Database.executeQuery(loginQuery, email, password);
            if (rs.next() && rs.getInt(1) == 1) {
                String uid = rs.getString("uid");
                String jti = createJti(uid);
                loginDTO = new LoginDTO(uid, jti);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginDTO;
    }

    private String createJti(String uid) throws SQLException {
        String query = Database.getQueryFromResource("createJTI.sql");
        ResultSet rs = Database.executeQuery(query, uid);
        if (rs.next()) {
            return rs.getString(1);
        } else {
            throw new SQLException();
        }
    }
}
