/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presence.services.rest;

import com.presence.dao.WatcherDAO;
import com.presence.beans.Watchers;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Suryaveer
 */
@Path("/watcher")
public class WatcherService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WatcherService.class);

    @POST
    @Consumes("application/json")
    public Response addWatcher(Watchers watcher) {
        int status = 0;
        WatcherDAO watcherDAO = new WatcherDAO();
        try {
            status = watcherDAO.insert(watcher);
            logger.debug("Record for watcher-presentity ({} - {}) processed with status: {} ", watcher.getWatcherUsername(), watcher.getPresentityUri(), status);
            return Response.status(201).build();
        } catch (Exception e) {
            logger.error("Error while processing watcher-presentity ({} - {}) ", watcher.getWatcherUsername(), watcher.getPresentityUri(), e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @GET
    @Path("/{watcherURI}/presentity/{presentityURI}")
    @Produces("application/json")
    public Response getWatcherDetails(@Context UriInfo uriInfo) {

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        MultivaluedMap<String, String> pathParameters = uriInfo.getPathParameters();
        WatcherDAO watcherDAO = new WatcherDAO();
        List<Watchers> watchersList;
        GenericEntity< List< Watchers>> entity;
        try {
            watchersList = watcherDAO.findByKey(queryParameters, pathParameters);
            Response.ResponseBuilder response;

            if (!watchersList.isEmpty()) {
                entity = new GenericEntity<List<Watchers>>(watchersList) {
                };
                response = Response.ok(entity);
            } else {
                response = Response.status(Response.Status.NOT_FOUND).entity("Requested resource could not be found.");
            }

            return response.build();
        } catch (Exception e) {
            logger.error("Error while fetching watcher {}.", pathParameters.getFirst("watcherURI"), e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @GET
    @Path("/presentity/{presentityURI}")
    @Produces("application/json")
    public Response getWatcherForPresentityByStatus(@Context UriInfo uriInfo) {

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        MultivaluedMap<String, String> pathParameters = uriInfo.getPathParameters();
        WatcherDAO watcherDAO = new WatcherDAO();
        List<Watchers> watchersList;
        GenericEntity< List< Watchers>> entity;
        try {
            if (uriInfo.getQueryParameters().containsKey("status") && uriInfo.getQueryParameters().containsKey("event")) {
                watchersList = watcherDAO.findByPresentityAndStatus(queryParameters, pathParameters);
            } else {
                watchersList = watcherDAO.findByPresentityAndEvent(queryParameters, pathParameters);
            }
            Response.ResponseBuilder response;

            if (!watchersList.isEmpty()) {
                entity = new GenericEntity<List<Watchers>>(watchersList) {
                };
                response = Response.ok(entity);
            } else {
                response = Response.status(Response.Status.NOT_FOUND).entity("Requested resource could not be found.");
            }

            return response.build();
        } catch (Exception e) {
            logger.error("Error while fetching watcher details for presentity {}.", pathParameters.getFirst("presentityURI"), e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @DELETE
    @Consumes("application/json")
    public Response deleteWatchers(@Context UriInfo uriInfo) {
        int status = 0;
        WatcherDAO watcherDAO = new WatcherDAO();
        try {
            if (uriInfo.getQueryParameters().isEmpty()) {
                status = watcherDAO.delete();
                logger.debug("Delete returned with status: {} ", status);
            } else {
                status = watcherDAO.deleteByStatusAndInsertTime(uriInfo.getQueryParameters());
                logger.debug("Delete by insertTime returned with status: {} ", status);
            }

            Response.ResponseBuilder response;
//Return 200 if any record deleted, 204 otherwise.            
            response = (status > 0 ? Response.ok() : Response.status(204));
            return response.build();
        } catch (Exception e) {
            logger.error("Error while processing delete watchers.", e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @DELETE
    @Path("/{watcherURI}/presentity/{presentityURI}")
    @Consumes("application/json")
    public Response deleteByKey(@Context UriInfo uriInfo) {
        int status = 0;
        WatcherDAO watcherDAO = new WatcherDAO();
        try {
            status = watcherDAO.deleteByKey(uriInfo.getQueryParameters(), uriInfo.getPathParameters());
            logger.debug("Delete for watcher {} and presentity {} returned with status: {} ", uriInfo.getPathParameters().getFirst("watcherURI"), uriInfo.getPathParameters().getFirst("presentityURI"), status);
            Response.ResponseBuilder response;
//Return 200 if any record deleted, 204 otherwise.            
            response = (status > 0 ? Response.ok() : Response.status(204));
            return response.build();
        } catch (Exception e) {
            logger.error("While delete for watcher {} and presentity {} returned with status: {} ", uriInfo.getPathParameters().getFirst("watcherURI"), uriInfo.getPathParameters().getFirst("presentityURI"), status, e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @PUT
    @Path("/{watcherURI}/presentity/{presentityURI}")
    @Consumes("application/json")
    public Response updateWatcher(@Context UriInfo uriInfo, Watchers watcher) {
        int status = 0;
        WatcherDAO watcherDAO = new WatcherDAO();
        try {
            status = watcherDAO.updateStatus(watcher, uriInfo.getQueryParameters(), uriInfo.getPathParameters());
            logger.debug("Record for watcher-presentity ({} - {}) processed with status: {} ", uriInfo.getQueryParameters().getFirst("watcherURI"), uriInfo.getQueryParameters().getFirst("presentityURI"), status);
            if (status > 0) {
                return Response.status(200).build();
            } else {
                return Response.status(201).entity("No record Updated").build();
            }
        } catch (Exception e) {
            logger.error("Record for watcher-presentity ({} - {}) processed with status: {} ", uriInfo.getQueryParameters().getFirst("watcherURI"), uriInfo.getQueryParameters().getFirst("presentityURI"), status, e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }
}
