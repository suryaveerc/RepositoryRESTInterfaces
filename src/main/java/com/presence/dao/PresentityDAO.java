package com.presence.dao;

import com.presence.model.Presentity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suryaveer on 7/20/2015.
 */
public class PresentityDAO {

    //Presentity Table: Primary key = id.
    //Unique index: username,domain,event,etag
    private static final String SQL_INSERT = "insert into presentity (domain,username,event,etag,expires,sender,body,received_time ) values "
            + "(?,?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "update presentity set etag=?,expires=?,received_time=?,sender=?,body=? where domain=? AND username=? AND event=? AND etag=?";
    // used for fetching result for a presentity
    private static final String SQL_SELECT = "select body,extra_hdrs,expires from presentity where domain=? AND username=? AND event=? AND etag=? order by received_time";
    // used for selecting expired publications.
    private static final String SQL_SELECT_BY_EXPIRES = "select username, domain, event, etag from presentity where expires<?";
    // used for notify
    private static final String SQL_SELECT_NO_ETAG = "select body,extra_hdrs,expires,etag from presentity where domain=? AND username=? AND event=? order by received_time";
    // used to fill the hash table at startup
    private static final String SQL_SELECT_ALL = "select username,domain,event,expires,etag from presentity";
    // used to delete presentities which send expires=0
    private static final String SQL_DELETE = "delete from presentity where domain=? AND username=? AND event=? AND etag=?";
    // used for deleting expired publications.
    private static final String SQL_DELETE_BY_EXPIRES = "delete from presentity where expires < ?";
    // used to check for presentity existence in DB.
    private static final String SQL_CHECK = "select count(*) from presentity where domain=? AND username=? AND event=? AND etag=?";
    // used to check if a presentity has presence info present
    private static final String SQL_CHECK_NO_ETAG = "select count(*) from presentity where domain=? AND username=? AND event=?";

    public int update(Presentity presentity, String etag, String domain, String event, String userName) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE);
            preparedStatement.setObject(1, presentity.getEtag());
            preparedStatement.setObject(2, presentity.getExpires());
            preparedStatement.setObject(3, presentity.getReceived_time());
            preparedStatement.setObject(4, presentity.getSender());
            preparedStatement.setObject(5, presentity.getBody());
            preparedStatement.setObject(6, domain);
            preparedStatement.setObject(7, userName);
            preparedStatement.setObject(8, event);
            preparedStatement.setObject(9, etag);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Error creating connection");
            e.printStackTrace();
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }
        return rowsAffected;
    }

    public int create(Presentity presentity) throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT);
            preparedStatement.setObject(1, presentity.getDomain());
            preparedStatement.setObject(2, presentity.getUsername());
            preparedStatement.setObject(3, presentity.getEvent());
            preparedStatement.setObject(4, presentity.getEtag());
            preparedStatement.setObject(5, presentity.getExpires());
            preparedStatement.setObject(6, presentity.getSender());
            preparedStatement.setObject(7, presentity.getBody());
            preparedStatement.setObject(8, presentity.getReceived_time());
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Error creating connection");
            e.printStackTrace();
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }
        return rowsAffected;
    }

    public List<Presentity> fetchAll(Integer expires) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Presentity> presentityList = new ArrayList<>();
        try {
            connection = DAOConnectionFactory.getConnection();
            if(expires!=null)
            {
                preparedStatement = connection.prepareStatement(SQL_SELECT_BY_EXPIRES);
                preparedStatement.setObject(1, expires);
            }
            else
                preparedStatement = connection.prepareStatement(SQL_SELECT_ALL);
            resultSet = preparedStatement.executeQuery();
            Presentity presentity;
            while (resultSet.next()) {
                System.out.println("In while");
                presentity = new Presentity();
                
                presentity.setUsername(resultSet.getString("username"));
                presentity.setDomain(resultSet.getString("domain"));
                presentity.setEtag(resultSet.getString("etag"));
                presentity.setEvent(resultSet.getString("event"));
                 if(expires==null)
                    presentity.setExpires(resultSet.getInt("expires"));
                presentityList.add(presentity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Error creating connection");
            e.printStackTrace();
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, resultSet);

        }
        return presentityList;
    }

    public List<Presentity> findByKey(String domain, String userName, String event, String etag) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Presentity> presentityList = new ArrayList<>();
        try {
            connection = DAOConnectionFactory.getConnection();
            if (etag != null) {
                preparedStatement = connection.prepareStatement(SQL_SELECT);
                preparedStatement.setObject(4, etag);
            } else {
                preparedStatement = connection.prepareStatement(SQL_SELECT_NO_ETAG);
            }
            preparedStatement.setObject(1, domain);
            preparedStatement.setObject(2, userName);
            preparedStatement.setObject(3, event);

            resultSet = preparedStatement.executeQuery();
            Presentity presentity;
            while (resultSet.next()) {
                System.out.println("In while");
                presentity = new Presentity();
                
                presentity.setBody(resultSet.getString("body"));
                presentity.setExpires(resultSet.getInt("expires"));
                presentity.setExtra_hdrs(resultSet.getString("extra_hdrs"));
                if(etag==null)
                    presentity.setEtag(resultSet.getString("etag"));
                presentityList.add(presentity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Error creating connection");
            e.printStackTrace();
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, resultSet);

        }
        return presentityList;
    }

    public List<Presentity> findByExpires(int expires) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Presentity> presentityList = new ArrayList<>();
        try {
            connection = DAOConnectionFactory.getConnection();

            preparedStatement = connection.prepareStatement(SQL_SELECT_BY_EXPIRES);

            preparedStatement.setObject(1, expires);

            resultSet = preparedStatement.executeQuery();
            Presentity presentity;
            while (resultSet.next()) {
                presentity = new Presentity();
                
                presentity.setBody(resultSet.getString("username"));
                presentity.setDomain(resultSet.getString("domain"));
                presentity.setEvent(resultSet.getString("event"));
                presentity.setEtag(resultSet.getString("etag"));
                presentityList.add(presentity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Error creating connection");
            e.printStackTrace();
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, resultSet);

        }
        return presentityList;
    }

    public int delete(String domain, String userName, String event, String etag) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE);
            preparedStatement.setObject(1, domain);
            preparedStatement.setObject(2, userName);
            preparedStatement.setObject(3, event);
            preparedStatement.setObject(4, etag);
            rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Delete Staus: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Error creating connection");
            e.printStackTrace();
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }
        return rowsAffected;
    }
    
    public int deleteByExpires(Integer expires) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int rowsAffected = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_DELETE_BY_EXPIRES);
            preparedStatement.setObject(1, expires);
            rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Delete Staus: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Error creating connection");
            e.printStackTrace();
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, null);
        }
        return rowsAffected;
    }

    public Boolean check(String domain, String userName, String event, String etag) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int count = 0;
        try {
            connection = DAOConnectionFactory.getConnection();
            if (etag != null) {
                preparedStatement = connection.prepareStatement(SQL_CHECK);
                preparedStatement.setObject(4, etag);
            } else {
                preparedStatement = connection.prepareStatement(SQL_CHECK_NO_ETAG);
            }
            preparedStatement.setObject(1, domain);
            preparedStatement.setObject(2, userName);
            preparedStatement.setObject(3, event);
//            System.out.println(preparedStatement.toString());
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            System.out.println("Now of records: " + count);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Error creating connection");
            e.printStackTrace();
            throw e;
        } finally {
            DAOConnectionFactory.closeConnection(connection, preparedStatement, rs);
        }
        return (count > 0);
    }
}
