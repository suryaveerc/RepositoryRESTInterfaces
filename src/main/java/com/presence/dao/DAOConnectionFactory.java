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
import org.slf4j.LoggerFactory;

/**
 * Created by Suryaveer on 7/20/2015.
 */
public class DAOConnectionFactory {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DAOConnectionFactory.class);
    private static final String databaseJNDI = "jdbc/presencedb";
    private static DataSource dataSource = null;
    private static Context context = null;
    private static Connection conn = null;

    private DAOConnectionFactory() {
        System.out.println("Initialized");
    }

    private static DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }
        try {
             context = new InitialContext();

            if (context == null) {
                context = new InitialContext();
            }
            dataSource = (DataSource) context.lookup("java:comp/env/"+databaseJNDI);
        } catch (NamingException e) {
            logger.error("Error while creating datasource.", e);
        }
        return dataSource;
    }

    protected static Connection getConnection() {
        try {
            conn = getDataSource().getConnection();
        } catch (SQLException e) {
            logger.error("Error while database connection.", e);
        }
        return conn;
    }

    protected static void closeConnection(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error("Error while closing resultset.", e);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("Error while closing statement.", e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Error while closing connection.", e);
            }
        }
    }
}
