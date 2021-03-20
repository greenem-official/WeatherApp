package com.mireaweb.app.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection to local database
 */

public class AbstractDAO {
    private String url = "jdbc:mysql://localhost/mirea?" + "useUnicode=true&serverTimezone=UTC" + "&user=root2&password=root2";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        return conn;
    }
}