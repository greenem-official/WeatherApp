package com.mireaweb.app.db.dao;

import java.sql.*;

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

    public void CloseConnections(ResultSet rs, PreparedStatement preparedStatement, Connection connection) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) {
            } // ignore
        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException sqlEx) {
            } // ignore
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqlEx) {
            } // ignore
        }
    }
}