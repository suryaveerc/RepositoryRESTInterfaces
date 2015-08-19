/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presence.dao;

import com.presence.model.ActiveWatchers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Suryaveer
 */
public class SubscriptionDAO {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionDAO.class);

//ActiveWatchers Table: Primary Key = id
//Unique index: presentity_uri,to_tag, from_tag, callid
    //select status,expires,watcher_username,watcher_domain,callid from active_watchers where presentity_uri=? AND event=?
    // insert new subscription.
    private static final String INSERT_SUBS = "insert into active_watchers (presentity_uri,callid,to_tag,from_tag,to_user,to_domain,watcher_username,"
            + "watcher_domain,event,event_id,local_cseq,remote_cseq,expires,status,reason,record_route,contact,local_contact,version,socket_info )"
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String UBDATE_SUBS_BY_EVENT = "update active_watchers set local_cseq=?,version=?,status=?,reason=? where presentity_uri=? "
            + "AND watcher_username=? AND watcher_domain=? AND event=? AND event_id=? AND callid=? AND to_tag=? AND from_tag=?";
    private static final String UBDATE_SUBS_BY_PRESENTITY = "update active_watchers set expires=?,local_cseq=?,remote_cseq=?,version=?,status=?,reason=?,contact=? where presentity_uri = ? AND callid = ? AND to_tag= ? AND from_tag=?";

    private static final String DELETE_SUBS_BY_EXPIRY = "delete from active_watchers where expires<?";

    private static final String DELETE_SUBS_BY_PRESENTITY = "delete from active_watchers where event=? AND to_tag=? AND presentity_uri=?";
    private static final String SELECT_ALL = "select presentity_uri,watcher_username,watcher_domain,to_user,to_domain,event,event_id,to_tag,from_tag,callid,local_cseq,remote_cseq,contact,record_route,expires,status,reason,version,socket_info,local_contact from active_watchers";
    //Used when looking for dialogs to send notifications.
    private static final String SELECT_BY_PRESENTITYURI = "select to_user,to_domain,watcher_username,watcher_domain,event_id,from_tag,to_tag,callid,local_cseq,record_route,contact,expires,reason,socket_info,local_contact,version from active_watchers where presentity_uri=? AND event=? AND status=? AND contact!=?";

    public int insertSubscription(ActiveWatchers activeWatchers) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int index = 0;

        logger.debug(activeWatchers.toString());
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(INSERT_SUBS);
            preparedStatement.setObject(++index, activeWatchers.getPresentityURI());
            preparedStatement.setObject(++index, activeWatchers.getCallId());
            preparedStatement.setObject(++index, activeWatchers.getToTag());
            preparedStatement.setObject(++index, activeWatchers.getFromTag());
            preparedStatement.setObject(++index, activeWatchers.getToUser());
            preparedStatement.setObject(++index, activeWatchers.getToDomain());
            preparedStatement.setObject(++index, activeWatchers.getWatcherUsername());
            preparedStatement.setObject(++index, activeWatchers.getWatcherDomain());
            preparedStatement.setObject(++index, activeWatchers.getEvent());
            preparedStatement.setObject(++index, activeWatchers.getEventId());
            preparedStatement.setObject(++index, activeWatchers.getLocalCseq());
            preparedStatement.setObject(++index, activeWatchers.getRemoteCseq());
            preparedStatement.setObject(++index, activeWatchers.getExpires());
            preparedStatement.setObject(++index, activeWatchers.getStatus());
            preparedStatement.setObject(++index, activeWatchers.getReason());
            preparedStatement.setObject(++index, activeWatchers.getRecordRoute());
            preparedStatement.setObject(++index, activeWatchers.getContact());
            preparedStatement.setObject(++index, activeWatchers.getLocalContact());
            preparedStatement.setObject(++index, activeWatchers.getVersion());
            preparedStatement.setObject(++index, activeWatchers.getSocketInfo());

            int status = preparedStatement.executeUpdate();
            logger.debug("Insert returned with status {}.", status);
            return status;

        } catch (SQLException ex) {
            logger.error("Error while adding new subscription by {}@{} for {} into database.", activeWatchers.getWatcherUsername(), activeWatchers.getWatcherDomain(), activeWatchers.getPresentityURI(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while adding new subscription by {}@{} for {} into database.", activeWatchers.getWatcherUsername(), activeWatchers.getWatcherDomain(), activeWatchers.getPresentityURI(), ex);
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }

    }

    public int updateSubscriptionByPresentity(ActiveWatchers activeWatchers, UriInfo uriInfo) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int index = 0;

        logger.debug(activeWatchers.toString());
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(UBDATE_SUBS_BY_PRESENTITY);
            //update columns
            preparedStatement.setObject(++index, activeWatchers.getExpires());
            preparedStatement.setObject(++index, activeWatchers.getLocalCseq());
            preparedStatement.setObject(++index, activeWatchers.getRemoteCseq());
            preparedStatement.setObject(++index, activeWatchers.getVersion());
            preparedStatement.setObject(++index, activeWatchers.getStatus());
            preparedStatement.setObject(++index, activeWatchers.getReason());
            preparedStatement.setObject(++index, activeWatchers.getContact());
            //where clause columns
            preparedStatement.setObject(++index, uriInfo.getPathParameters().getFirst("presentityID"));
            preparedStatement.setObject(++index, uriInfo.getQueryParameters().getFirst("call_id"));
            preparedStatement.setObject(++index, uriInfo.getQueryParameters().getFirst("to_tag"));
            preparedStatement.setObject(++index, uriInfo.getQueryParameters().getFirst("from_tag"));

            int status = preparedStatement.executeUpdate();
            logger.debug("Update returned with status {}.", status);
            return status;

        } catch (SQLException ex) {
            logger.error("Error while updating subscription by {}@{} for {} into database.", activeWatchers.getWatcherUsername(), activeWatchers.getWatcherDomain(), activeWatchers.getPresentityURI(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while updating subscription by {}@{} for {} into database.", activeWatchers.getWatcherUsername(), activeWatchers.getWatcherDomain(), activeWatchers.getPresentityURI(), ex);
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }

    }

    public int updateSubscriptionByEvent(ActiveWatchers activeWatchers, UriInfo uriInfo) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int index = 0;
        int separatorIndex = uriInfo.getPathParameters().getFirst("watcherID").indexOf('@');
        //ogger.debug(activeWatchers.toString());
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(UBDATE_SUBS_BY_EVENT);
            //update columns
            preparedStatement.setObject(++index, activeWatchers.getLocalCseq());
            preparedStatement.setObject(++index, activeWatchers.getVersion());
            preparedStatement.setObject(++index, activeWatchers.getStatus());
            preparedStatement.setObject(++index, activeWatchers.getReason());
            //where clause columns
            preparedStatement.setObject(++index, uriInfo.getPathParameters().getFirst("presentityID"));
            preparedStatement.setObject(++index, uriInfo.getPathParameters().getFirst("watcherID").substring(0, separatorIndex));
            preparedStatement.setObject(++index, uriInfo.getPathParameters().getFirst("watcherID").substring(separatorIndex + 1));
            preparedStatement.setObject(++index, uriInfo.getQueryParameters().getFirst("event"));
            preparedStatement.setObject(++index, uriInfo.getQueryParameters().getFirst("event_id"));
            preparedStatement.setObject(++index, uriInfo.getQueryParameters().getFirst("callid"));
            preparedStatement.setObject(++index, uriInfo.getQueryParameters().getFirst("to_tag"));
            preparedStatement.setObject(++index, uriInfo.getQueryParameters().getFirst("from_tag"));

            int status = preparedStatement.executeUpdate();
            logger.debug("Update returned with status {}.", status);
            return status;

        } catch (SQLException ex) {
            logger.error("Error while updating subscription by {}@{} for {} into database.", activeWatchers.getWatcherUsername(), activeWatchers.getWatcherDomain(), activeWatchers.getPresentityURI(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while updating subscription by {}@{} for {} into database.", activeWatchers.getWatcherUsername(), activeWatchers.getWatcherDomain(), activeWatchers.getPresentityURI(), ex);
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }

    }

    public List<ActiveWatchers> findByPresentityURI(MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> pathParameters) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int index = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_BY_PRESENTITYURI);
            preparedStatement.setObject(++index, pathParameters.getFirst("presentityURI"));
            preparedStatement.setObject(++index, queryParameters.getFirst("event"));
            preparedStatement.setObject(++index, queryParameters.getFirst("status"));
            preparedStatement.setObject(++index, queryParameters.getFirst("contact"));

            resultSet = preparedStatement.executeQuery();
            List<ActiveWatchers> activeWatchersList = new ArrayList<>();
            ActiveWatchers activeWatchers;
            while (resultSet.next()) {
//                index = 0;
                activeWatchers = new ActiveWatchers();
                activeWatchers.setToUser(resultSet.getString(1));
                activeWatchers.setToDomain(resultSet.getString(2));
                activeWatchers.setWatcherUsername(resultSet.getString(3));
                activeWatchers.setWatcherDomain(resultSet.getString(4));
                activeWatchers.setEventId(resultSet.getString(5));
                activeWatchers.setFromTag(resultSet.getString(6));
                activeWatchers.setToTag(resultSet.getString(7));
                activeWatchers.setCallId(resultSet.getString(8));
                activeWatchers.setLocalCseq(resultSet.getInt(9));
                activeWatchers.setRecordRoute(resultSet.getString(10));
                activeWatchers.setContact(resultSet.getString(11));
                activeWatchers.setExpires(resultSet.getInt(12));
                activeWatchers.setReason(resultSet.getString(13));
                activeWatchers.setSocketInfo(resultSet.getString(14));
                activeWatchers.setLocalContact(resultSet.getString(15));
                activeWatchers.setVersion(resultSet.getInt(16));
                activeWatchersList.add(activeWatchers);
            }
            logger.debug("Total {} dialogs fetched from database for presentity {}.", activeWatchersList.size(), pathParameters.getFirst("presentityURI"));
            return activeWatchersList;
        } catch (SQLException ex) {
            logger.error("Error while fetching dialogs created for presentity {}.", pathParameters.getFirst("presentityURI"), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while fetching dialogs created for presentity {}.", pathParameters.getFirst("presentityURI"), ex);
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, resultSet);
        }
    }

    public List<ActiveWatchers> findAll() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int index = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SELECT_ALL);

            resultSet = preparedStatement.executeQuery();
            List<ActiveWatchers> activeWatchersList = new ArrayList<>();
            ActiveWatchers activeWatchers;
            while (resultSet.next()) {
//                index = 0;
                activeWatchers = new ActiveWatchers();
                activeWatchers.setPresentityURI(resultSet.getString(1));
                activeWatchers.setWatcherUsername(resultSet.getString(2));
                activeWatchers.setWatcherDomain(resultSet.getString(3));
                activeWatchers.setToUser(resultSet.getString(4));
                activeWatchers.setToDomain(resultSet.getString(5));
                activeWatchers.setEvent(resultSet.getString(6));
                activeWatchers.setEventId(resultSet.getString(7));
                activeWatchers.setToTag(resultSet.getString(8));
                activeWatchers.setFromTag(resultSet.getString(9));
                activeWatchers.setCallId(resultSet.getString(10));
                activeWatchers.setLocalCseq(resultSet.getInt(11));
                activeWatchers.setRemoteCseq(resultSet.getInt(12));
                activeWatchers.setContact(resultSet.getString(13));
                activeWatchers.setRecordRoute(resultSet.getString(14));
                activeWatchers.setExpires(resultSet.getInt(15));
                activeWatchers.setStatus(resultSet.getInt(16));
                activeWatchers.setReason(resultSet.getString(17));
                activeWatchers.setVersion(resultSet.getInt(18));
                activeWatchers.setSocketInfo(resultSet.getString(19));
                activeWatchers.setLocalContact(resultSet.getString(20));
                activeWatchersList.add(activeWatchers);
            }
            logger.debug("Total {} dialogs fetched from database.", activeWatchersList.size());
            return activeWatchersList;
        } catch (SQLException ex) {
            logger.error("Error while fetching dialogs.", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while fetching dialogs.", ex);
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, resultSet);
        }
    }

    public int deleteSubscriptionByPresentity(MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> pathParameters) throws SQLException {
        String presentityURI = pathParameters.getFirst("presentityID");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int index = 0;
        int status = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(DELETE_SUBS_BY_PRESENTITY);

            preparedStatement.setObject(++index, queryParameters.getFirst("event"));
            preparedStatement.setObject(++index, queryParameters.getFirst("to_tag"));
            preparedStatement.setObject(++index, presentityURI);

            status = preparedStatement.executeUpdate();
            logger.debug("Delete Return status: {}", status);
            return status;
        } catch (SQLException ex) {
            logger.error("Error while deleting subscriptions for : {} ", presentityURI, ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while deleting subscriptions for : {} ", presentityURI, ex);
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }
    }

    public int deleteSubscriptionByQuery(MultivaluedMap<String, String> queryParameters) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int status = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            if (queryParameters.containsKey("expires")) {
                preparedStatement = connection.prepareStatement(DELETE_SUBS_BY_EXPIRY);
                preparedStatement.setObject(1, queryParameters.getFirst("expires"));
            }
            status = preparedStatement.executeUpdate();
            logger.debug("Delete Return status: {}", status);
            return status;
        } catch (SQLException ex) {
            logger.error("Error while deleting subscriptions.", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error while deleting subscriptions.", ex);
            throw ex;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }
    }
}