/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presence.dao;

import com.presence.model.Watchers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Suryaveer
 */
public class WatcherDAO {

    private static final Logger logger = LoggerFactory.getLogger(WatcherDAO.class);
    //Watcher Table: Primary key = id.
    //Unique index: presentity_uri,watcher_username,event,watcher_domain
    private static final String SELECT_STATUS_BY_KEY = "select status,reason from watchers where presentity_uri=? AND watcher_username=? AND watcher_domain=? AND event=?";

    private static final String INSERT_WATCHER = "insert into watchers (presentity_uri,watcher_username,watcher_domain,event,status,inserted_time,reason ) values (?,?,?,?,?,?,?)";

    public List<Watchers> findWatcherStatusByKey(String presentityURI, String watcherUserName, String watcherDomain, String event) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Watchers> watchersList = new ArrayList<>();
        int index=0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_STATUS_BY_KEY);
            preparedStatement.setObject(++index, presentityURI);
            preparedStatement.setObject(++index, watcherUserName);
            preparedStatement.setObject(++index, watcherDomain);
            preparedStatement.setObject(++index, event);
            resultSet = preparedStatement.executeQuery();
            Watchers watchers;
            while (resultSet.next()) {
                System.out.println("In while");
                watchers = new Watchers();

                watchers.setStatus(resultSet.getInt("status"));
                watchers.setReason(resultSet.getString("reason"));
                watchersList.add(watchers);
            }
        } catch (SQLException ex) {
            logger.error("List<Watchers>", ex);
            throw ex;
        } catch (Exception e) {
            logger.error("addWatcher", e);
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, resultSet);
        }
        return watchersList;

    }

    public int addWatcher(Watchers watcher) throws SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int status = 0;
        int index = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_WATCHER);
            preparedStatement.setObject(++index, watcher.getPresentityUri());
            preparedStatement.setObject(++index, watcher.getWatcherUsername());
            preparedStatement.setObject(++index, watcher.getWatcherDomain());
            preparedStatement.setObject(++index, watcher.getEvent());
            preparedStatement.setObject(++index, watcher.getStatus());
            preparedStatement.setObject(++index, watcher.getInsertedTime());
            preparedStatement.setObject(++index, watcher.getReason());
            status = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("addWatcher", ex);
            throw ex;
        } catch (Exception e) {
            logger.error("addWatcher", e);
            throw e;
        }
        return status;
    }
}
