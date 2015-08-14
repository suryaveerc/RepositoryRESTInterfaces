/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presence.services.rest;

import com.presence.dao.SubscriptionDAO;
import com.presence.model.ActiveWatchers;
import java.util.List;
import javax.ws.rs.Consumes;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Suryaveer
 */
@Path("/V1/subscription")
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @POST
    @Consumes("application/json")
    public Response addSubscription(ActiveWatchers activeWatchers) {
        int status = 0;
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        try {
            status = subscriptionDAO.insertSubscription(activeWatchers);
            return Response.status(201).build();
        } catch (Exception ex) {
            logger.error("Error while creating subscription.", ex);
            return Response.status(500).entity("Server Error").build();
        }
    }

    @PUT
    @Path("/{watcherID}/{presentityID}")
    @Consumes("application/json")
    public Response updateSubscription(ActiveWatchers activeWatchers, @Context UriInfo uriInfo) {
        int status = 0;
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        try {
            status = subscriptionDAO.updateSubscriptionByEvent(activeWatchers, uriInfo);
            return Response.status(201).build();
        } catch (Exception ex) {
            logger.error("Error while creating subscription.", ex);
            return Response.status(500).entity("Server Error").build();
        }
    }

    @GET
    @Path("{watcherURI}/{presentityURI}")
    @Produces("application/json")
    public Response getSubscription(@Context UriInfo uriInfo) {

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        MultivaluedMap<String, String> pathParameters = uriInfo.getPathParameters();

        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        List activeWatchersList;
        GenericEntity<List<ActiveWatchers>> entity;
        try {
            if (queryParameters.size() == 0) {
                activeWatchersList = subscriptionDAO.findAll();
            } else if (queryParameters.containsKey("status")) {
                activeWatchersList = subscriptionDAO.findByPresentityURI(queryParameters, pathParameters);
            }
            else
                activeWatchersList = null;
            
            if(activeWatchersList.isEmpty())
            {
                return Response.status(404).entity("No active watchers present.").build();
            }
            else
            {
                entity = new GenericEntity<List<ActiveWatchers>>(activeWatchersList){};
                return Response.ok(entity).build();
            }
        } catch (Exception e) {
            logger.error("Error while fetching dialogs.", e);
            return Response.status(500).entity("Server Error").build();
        }
    }

}
