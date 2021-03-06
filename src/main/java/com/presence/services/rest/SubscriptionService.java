/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presence.services.rest;

import com.presence.beans.ActiveWatchers;
import com.presence.dao.SubscriptionDAO;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Suryaveer
 */
@Path("/subscription")
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);
    private SubscriptionDAO subscriptionDAO;

    @POST
    @Consumes("application/json")
    public Response addSubscription(ActiveWatchers activeWatchers) {
        long start = System.currentTimeMillis();
        subscriptionDAO = new SubscriptionDAO();
        try {
            subscriptionDAO.insertSubscription(activeWatchers);
            long end = System.currentTimeMillis();
            logger.debug("addSubscription elasped time {}", (end - start));
            return Response.status(201).build();
        } catch (Exception ex) {
            logger.error("Error while creating subscription.", ex);
            return Response.status(500).entity("Server Error").build();
        }
    }

    @PUT
    @Path("/watcher/{watcherID}/{presentityID}")
    @Consumes("application/json")
    public Response updateSubscription(ActiveWatchers activeWatchers, @Context UriInfo uriInfo) {
        long start = System.currentTimeMillis();
        int status = 0;
        subscriptionDAO = new SubscriptionDAO();
        try {

            status = subscriptionDAO.updateSubscriptionByEvent(activeWatchers, uriInfo);

            if (status == 0) {
                logger.debug("No subscriptions were updated for watcher {}.", uriInfo.getPathParameters().getFirst("watcherID"));
                long end = System.currentTimeMillis();
                logger.debug(" updateSubscription elasped time {}", (end - start));
                return Response.status(204).entity("No record updated.").build();
            } else {
                long end = System.currentTimeMillis();
                logger.debug(" updateSubscription elasped time {}", (end - start));
                return Response.status(200).build();
            }
        } catch (Exception ex) {
            logger.error("Error while updating subscription for {}.", uriInfo.getPathParameters().getFirst("watcherID"), ex);
            return Response.status(500).entity("Server Error").build();
        }
    }

    @PUT
    @Path("/presentity/{presentityID}")
    @Consumes("application/json")
    public Response updateSubscriptionByPresentity(ActiveWatchers activeWatchers, @Context UriInfo uriInfo) {
        int status = 0;

        subscriptionDAO = new SubscriptionDAO();
        try {
            long start = System.currentTimeMillis();
            status = subscriptionDAO.updateSubscriptionByPresentity(activeWatchers, uriInfo);
            long end = System.currentTimeMillis();
            logger.debug("elasped time {}", (end - start));
            if (status == 0) {
                logger.debug("No subscriptions were updated for presentity {}.", uriInfo.getPathParameters().getFirst("presentityID"));
                return Response.status(204).entity("No record updated.").build();
            } else {
                return Response.status(200).build();
            }
        } catch (Exception ex) {
            logger.error("Error while updating subscription for {}.", uriInfo.getPathParameters().getFirst("watcherID"), ex);
            return Response.status(500).entity("Server Error").build();
        }
    }

    @GET
    @Produces("application/json")
    public Response getSubscription() {
        long start = System.currentTimeMillis();
        subscriptionDAO = new SubscriptionDAO();
        List activeWatchersList;
        GenericEntity<List<ActiveWatchers>> entity;
        try {

            activeWatchersList = subscriptionDAO.findAll();

            if (activeWatchersList.isEmpty()) {
                long end = System.currentTimeMillis();
                logger.debug("getSubscription elasped time {}", (end - start));
                return Response.status(404).entity("No active watchers present.").build();

            } else {
                entity = new GenericEntity<List<ActiveWatchers>>(activeWatchersList) {
                };
                long end = System.currentTimeMillis();
                logger.debug("getSubscription elasped time {}", (end - start));
                return Response.ok(entity).build();
            }
        } catch (Exception e) {
            logger.error("Error while fetching all dialogs.", e);
            return Response.status(500).entity("Server Error").build();
        }
    }

    @GET
    @Path("/watcher/{watcherID}")
    @Produces("application/json")
    public Response getSubscriptionByWatcher(@Context UriInfo uriInfo) {
        long start = System.currentTimeMillis();
        subscriptionDAO = new SubscriptionDAO();
        List activeWatchersList;
        GenericEntity<List<ActiveWatchers>> entity;
        try {

            activeWatchersList = subscriptionDAO.findByWatcherURI(uriInfo.getQueryParameters(), uriInfo.getPathParameters());

            if (activeWatchersList.isEmpty()) {
                long end = System.currentTimeMillis();
                logger.debug("getSubscriptionByWatcher elasped time {}", (end - start));
                return Response.status(404).entity("No active watchers present.").build();
            } else {
                entity = new GenericEntity<List<ActiveWatchers>>(activeWatchersList) {
                };
                long end = System.currentTimeMillis();
                logger.debug("getSubscriptionByWatcher elasped time {}", (end - start));
                return Response.ok(entity).build();
            }
        } catch (Exception e) {
            logger.error("Error while fetching dialogs by watcher: {}.", uriInfo.getPathParameters().getFirst("watcherID"), e);
            return Response.status(500).entity("Server Error").build();
        }
    }

    @GET
    @Path("/presentity/{presentityID}")
    @Produces("application/json")
    public Response getSubscriptionByPresentity(@Context UriInfo uriInfo) {

        subscriptionDAO = new SubscriptionDAO();
        List activeWatchersList;
        GenericEntity<List<ActiveWatchers>> entity;
        try {

            if (uriInfo.getQueryParameters().containsKey("status") && uriInfo.getQueryParameters().containsKey("contact")) {
                long start = System.currentTimeMillis();
                activeWatchersList = subscriptionDAO.findByPresentityURI(uriInfo.getQueryParameters(), uriInfo.getPathParameters());

                long end = System.currentTimeMillis();
                logger.debug("elasped time {}", (end - start));
            } else {
                long start = System.currentTimeMillis();
                activeWatchersList = subscriptionDAO.findByPresentityAndEvent(uriInfo.getQueryParameters(), uriInfo.getPathParameters());

                long end = System.currentTimeMillis();
                logger.debug("elasped time {}", (end - start));
            }

            if (activeWatchersList.isEmpty()) {
                return Response.status(404).entity("No active watchers present.").build();
            } else {
                entity = new GenericEntity<List<ActiveWatchers>>(activeWatchersList) {
                };
                return Response.ok(entity).build();
            }
        } catch (Exception e) {
            logger.error("Error while fetching all dialogs.", e);
            return Response.status(500).entity("Server Error").build();
        }
    }

    @DELETE
    public Response deleteSubscription(@Context UriInfo uriInfo) {

        try {
            subscriptionDAO = new SubscriptionDAO();
            long start = System.currentTimeMillis();
            int status = subscriptionDAO.deleteSubscriptionByQuery(uriInfo.getQueryParameters());
            long end = System.currentTimeMillis();
            logger.debug("elasped time {}", (end - start));
            ResponseBuilder response;
//Return 200 if any record deleted, 204 otherwise.            
            response = (status > 0 ? Response.ok() : Response.status(204));
            return response.build();
        } catch (Exception e) {
            logger.error("Error while deleting subscriptions.", e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @DELETE
    @Path("/presentity/{presentityID}")
    public Response deleteSubscriptionByPresentity(@Context UriInfo uriInfo) {
        try {
            subscriptionDAO = new SubscriptionDAO();
            long start = System.currentTimeMillis();
            int status = subscriptionDAO.deleteSubscriptionByPresentity(uriInfo.getQueryParameters(), uriInfo.getPathParameters());

            long end = System.currentTimeMillis();
            logger.debug("elasped time {}", (end - start));
            ResponseBuilder response;
//Return 200 if any record deleted, 204 otherwise.            
            response = (status > 0 ? Response.ok() : Response.status(204));
            return response.build();
        } catch (Exception e) {
            logger.error("Error while deleting subscriptions.", e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    //This function is not used directly, means, used to delete from when xcap is used.
    @DELETE
    @Path("/{watcherID}/{presentityID}")
    public Response deleteSubscriptionByWatcherAndPresentity(@Context UriInfo uriInfo) {
        try {
            subscriptionDAO = new SubscriptionDAO();
            long start = System.currentTimeMillis();
            int status = subscriptionDAO.deleteSubscriptionPresentityAndWatcher(uriInfo.getQueryParameters(), uriInfo.getPathParameters());

            long end = System.currentTimeMillis();
            logger.debug("elasped time {}", (end - start));
            logger.debug("Delete subscription for watcher {} and presentity {} returned with status: {} ", uriInfo.getPathParameters().getFirst("watcherID"), uriInfo.getPathParameters().getFirst("presentityID"), status);
            ResponseBuilder response;
//Return 200 if any record deleted, 204 otherwise.            
            response = (status > 0 ? Response.ok() : Response.status(204));
            return response.build();
        } catch (Exception e) {
            logger.error("Error while deleting subscription for watcher {} and presentity {}. ", uriInfo.getPathParameters().getFirst("watcherID"), uriInfo.getPathParameters().getFirst("presentityID"), e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }
}
