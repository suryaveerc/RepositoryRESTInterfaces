package com.presence.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Suryaveer on 7/20/2015.
 */
public class DAOConnectionFactory {

    private static final String databaseJNDI = "";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://192.168.254.130:3306/opensips";
    // Database credentials
    private static final String USER = "root";
    private static final String PASS = "";
    private static DataSource dataSource = null;
    private static Context context = null;
    private static Connection conn = null;

    private DAOConnectionFactory() {
        System.out.println("Initialized");
    }

    private static DataSource createConnection() {

        if (dataSource != null) {
            return dataSource;
        }

        try {
            if (context == null) {
                context = new InitialContext();
            }
            dataSource = (DataSource) (context).lookup(databaseJNDI);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    protected static Connection getConnection() {
      
        try {
            if (databaseJNDI == "") {
                System.out.println("Creating database connection using DriverManager");
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
            } else {
                System.out.println("Creating database connection using DriverManager");
                  if (conn != null) {
            return conn;
        }
                conn = createConnection().getConnection();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    protected static void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }
}
