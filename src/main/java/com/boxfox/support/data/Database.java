package com.boxfox.support.data;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private static PreparedStatement buildQuery(String sql, Object... args) {
        PreparedStatement statement = null;
        try {
            statement = DataSource.getConnection().prepareStatement(sql);
            int placeholderCount = 1;
            for (Object o : args) {
                statement.setObject(placeholderCount++, o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statement;
    }

    public static ResultSet executeQuery(String sql, Object... args) throws SQLException {
        return buildQuery(sql, args).executeQuery();
    }

    public static int executeUpdate(String sql, Object... args) throws SQLException {
        return buildQuery(sql, args).executeUpdate();
    }

    public static String getQueryFromResource(String resourceName) {
        try {
            return Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}