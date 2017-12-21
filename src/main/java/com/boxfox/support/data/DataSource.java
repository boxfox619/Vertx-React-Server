package com.boxfox.support.data;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private BasicDataSource ds;

    private DataSource(String host, String port, String dbName, String id, String pwd) {
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(id);
        ds.setPassword(pwd);
        ds.setUrl("jdbc:mysql://" + host + ":" + port + "/" + dbName);
    }

    private static class DataSourceInstance {
        private static String host = Config.getDefaultInstance().getString("mysqlHost", "localhost");
        private static String user = Config.getDefaultInstance().getString("mysqlUser");
        private static String password = Config.getDefaultInstance().getString("mysqlPassword");
        private static String port = Config.getDefaultInstance().getString("mysqlPort", "3306");
        private static String dbName = Config.getDefaultInstance().getString("mysqlDBName");
        private static DataSource instance = new DataSource(host, port, dbName, user, password);
    }

    protected static Connection getConnection() throws SQLException {
        return DataSourceInstance.instance.ds.getConnection();
    }
}
