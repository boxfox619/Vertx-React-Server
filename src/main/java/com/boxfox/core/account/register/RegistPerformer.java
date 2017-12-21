package com.boxfox.core.account.register;

import com.boxfox.support.data.AbstractDAO;
import com.boxfox.support.data.Database;

import java.sql.SQLException;

public class RegistPerformer extends AbstractDAO {

    public boolean register(String email, String password, String nickname) {
        boolean result = false;
        String registerQuery = Database.getQueryFromResource("register.sql");
        try {
            int count = Database.executeUpdate(registerQuery, email, password, nickname);
            if (count > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
