/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presence.dao;

import com.presence.beans.Watchers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
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

    // used to check watcher authorization status for new subs request.
    private static final String SELECT_STATUS_BY_KEY = "select status,reason from watchers where presentity_uri=? AND watcher_username=? AND watcher_domain=? AND event=?";
    // insert new watcher
    private static final String INSERT_WATCHER = "insert into watchers (presentity_uri,watcher_username,watcher_domain,event,status,inserted_time,reason ) values (?,?,?,?,?,?,?)";

    private static final String SELECT_BY_PRES_AND_STATUS = "select watcher_username,watcher_domain from watchers where presentity_uri=? AND event=? AND status=?";
    //used to clean the table at server startup.
    private static final String DELETE = "delete from watchers";

    private static final String SELECT_BY_PRES_AND_EVENT = "select watcher_username,watcher_domain,status,reason from watchers where presentity_uri=? AND event=?";
    private static final String UPDATE_STATUS_BY_KEY = "update watchers set status=?, reason=? where presentity_uri = ? AND watcher_username = ? AND watcher_domain = ? and event =?";
    private static final String DELETE_BY_STATUS_INSERT_TIME = "delete from watchers where inserted_time< ? AND status= ?";
    private static final String DELETE_BY_KEY = "delete from watchers where presentity_uri = ? AND watcher_username = ? AND watcher_domain = ? and event =?";

    public int insert(Watchers watcher) throws SQLException {

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
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }
        return status;
    }

    public List<Watchers> findByKey(MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> pathParameters) throws SQLException {

        int index = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int status = 0;
        String watcherURI = null;

        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_STATUS_BY_KEY);
            watcherURI = pathParameters.getFirst("watcherURI");
            String userName = watcherURI.substring(0, watcherURI.indexOf('@'));
            String domain = watcherURI.substring(watcherURI.indexOf('@') + 1);

            preparedStatement.setObject(++index, pathParameters.getFirst("presentityURI"));
            preparedStatement.setObject(++index, userName);
            preparedStatement.setObject(++index, domain);
            preparedStatement.setObject(++index, queryParameters.getFirst("event"));

            rs = preparedStatement.executeQuery();
            Watchers watchers = new Watchers();
            List<Watchers> watchersList = new ArrayList<>();
            while (rs.next()) {
                watchers.setStatus(rs.getInt(1));
                watchers.setReason(rs.getString(2));
                watchersList.add(watchers);
            }
            return watchersList;
        } catch (SQLException ex) {
            logger.error("Error while fetching watcher {}.", watcherURI);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while fetching watcher {}.", watcherURI);
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, rs);
        }
    }

    public List<Watchers> findByPresentityAndStatus(MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> pathParameters) throws SQLException {

        int index = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int status = 0;
        String watcherURI = null;

        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_BY_PRES_AND_STATUS);

            preparedStatement.setObject(++index, pathParameters.getFirst("presentityURI"));
            preparedStatement.setObject(++index, queryParameters.getFirst("event"));
            preparedStatement.setObject(++index, queryParameters.getFirst("status"));

            rs = preparedStatement.executeQuery();
            Watchers watchers;
            List<Watchers> watchersList = new ArrayList<>();
            while (rs.next()) {
                watchers = new Watchers();
                watchers.setWatcherUsername(rs.getString(1));
                watchers.setWatcherDomain(rs.getString(2));
                watchersList.add(watchers);
            }
            logger.debug("Query returned {} results.", watchersList.size());
            return watchersList;
        } catch (SQLException ex) {
            logger.error("Error while fetching watcher for presentity {}.", pathParameters.getFirst("presentityURI"));
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while fetching watcher for presentity {}.", pathParameters.getFirst("presentityURI"));
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, rs);
        }
    }

    public int delete() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int status = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(DELETE);
            status = preparedStatement.executeUpdate();
            logger.debug("Delete Return status: {}", status);
            return status;
        } catch (SQLException ex) {
            logger.error("Error while deleting watchers", ex);
            throw ex;
        } catch (Exception e) {
            logger.error("Error while deleting watchers", e);
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }

    }

    // Below 3 functions are not used in normal scenario. Used when XCAP is introduced.
    public List<Watchers> findByPresentityAndEvent(MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> pathParameters) throws SQLException {

        int index = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int status = 0;
        String watcherURI = null;

        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_BY_PRES_AND_EVENT);

            preparedStatement.setObject(++index, pathParameters.getFirst("presentityURI"));
            preparedStatement.setObject(++index, queryParameters.getFirst("event"));

            rs = preparedStatement.executeQuery();
            Watchers watchers;
            List<Watchers> watchersList = new ArrayList<>();
            while (rs.next()) {
                watchers = new Watchers();
                watchers.setWatcherUsername(rs.getString(1));
                watchers.setWatcherDomain(rs.getString(2));
                watchers.setStatus(rs.getInt(3));
                watchers.setReason(rs.getString(4));
                watchersList.add(watchers);
            }
            logger.debug("Query returned {} results.", watchersList.size());
            return watchersList;
        } catch (SQLException ex) {
            logger.error("Error while fetching watcher for presentity {}.", pathParameters.getFirst("presentityURI"));
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while fetching watcher for presentity {}.", pathParameters.getFirst("presentityURI"));
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, rs);
        }
    }

    public int updateStatus(Watchers watcher, MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> pathParameters) throws SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int status = 0;
        int index = 0;
        int separatorIndex = pathParameters.getFirst("watcherURI").indexOf('@');

        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(UPDATE_STATUS_BY_KEY);
            preparedStatement.setObject(1, watcher.getStatus());
            preparedStatement.setObject(2, watcher.getReason());
            preparedStatement.setObject(3, pathParameters.getFirst("presentityURI"));
            preparedStatement.setObject(4, pathParameters.getFirst("watcherURI").substring(0, separatorIndex));
            preparedStatement.setObject(5, pathParameters.getFirst("watcherURI").substring(separatorIndex + 1));
            preparedStatement.setObject(6, queryParameters.getFirst("event"));
            status = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error("updating watcher status {} ", pathParameters.getFirst("watcherURI"), ex);
            throw ex;
        } catch (Exception e) {
            logger.error("updating watcher status {} ", pathParameters.getFirst("watcherURI"), e);
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }
        return status;
    }

    public int deleteByStatusAndInsertTime(MultivaluedMap<String, String> queryParameters) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int index = 0;
        int status = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_BY_STATUS_INSERT_TIME);
            preparedStatement.setObject(++index, queryParameters.getFirst("inserted_time"));
            preparedStatement.setObject(++index, queryParameters.getFirst("status"));
            status = preparedStatement.executeUpdate();
            logger.debug("Delete Return status: {}", status);
            return status;
        } catch (SQLException ex) {
            logger.error("Error while deleting watchers by insert_time: {}and status: {} ", queryParameters.getFirst("inserted_time"), queryParameters.getFirst("status"), ex);
            throw ex;
        } catch (Exception e) {
            logger.error("Error while deleting watchers by insert_time: {}and status: {} ", queryParameters.getFirst("inserted_time"), queryParameters.getFirst("status"), e);
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }

    }

    public int deleteByKey(MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> pathParameters) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int status = 0;
        int separatorIndex = pathParameters.getFirst("watcherURI").indexOf('@');
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_BY_KEY);
            preparedStatement.setObject(1, pathParameters.getFirst("presentityURI"));
            preparedStatement.setObject(2, pathParameters.getFirst("watcherURI").substring(0, separatorIndex));
            preparedStatement.setObject(3, pathParameters.getFirst("watcherURI").substring(separatorIndex + 1));
            preparedStatement.setObject(4, queryParameters.getFirst("event"));

            status = preparedStatement.executeUpdate();
            logger.debug("Delete Return status: {}", status);
            return status;
        } catch (SQLException ex) {
            logger.error("Error while deleting watchers by insert_time: {}and status: {} ", queryParameters.getFirst("inserted_time"), queryParameters.getFirst("status"), ex);
            throw ex;
        } catch (Exception e) {
            logger.error("Error while deleting watchers by insert_time: {}and status: {} ", queryParameters.getFirst("inserted_time"), queryParameters.getFirst("status"), e);
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }

    }

}
