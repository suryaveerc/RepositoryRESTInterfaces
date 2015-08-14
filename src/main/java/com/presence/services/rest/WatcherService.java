/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presence.services.rest;

import com.presence.dao.WatcherDAO;
import com.presence.model.Watchers;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("/V1/watcher")
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
    @Path("/{watcherURI}/{presentityURI}")
    @Produces("application/json")
    public Response getPresentityListForWatcher(@Context UriInfo uriInfo) {
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
}
