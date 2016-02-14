package com.presence.services.rest;

import com.presence.beans.Presentity;
import com.presence.dao.PresentityDAO;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.slf4j.LoggerFactory;

/**
 * Created by Suryaveer on 7/20/2015.
 */
@Path("/presentity")
public class PresentityService {

    /* This method allows to insert new presentity into the repository */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PresentityService.class);
    private PresentityDAO presentityDAO;
    private String userName;
    private String domain;
    @POST
    @Consumes("application/json")
    public Response addPresentity(Presentity presentity) {
        presentityDAO = new PresentityDAO();
        try {
            presentityDAO.insert(presentity);
            return Response.status(201).build();
        } catch (Exception e) {
            logger.error("Error while sending request to database", e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }
    /* This method allows to update new presentity into the repository */

    @PUT
    @Path("/{presentityId}")
    @Consumes("application/json")
    public Response updatePresentity(@PathParam("presentityId") String presentityId, Presentity presentity, @QueryParam("event") String event, @QueryParam("etag") String etag) {

        userName = presentityId.substring(0, presentityId.indexOf('@'));
        domain = presentityId.substring(presentityId.indexOf('@') + 1);
        presentityDAO = new PresentityDAO();
        logger.debug(presentity.toString());
        /*RFC2616 If an existing resource is modified, either the 200 (OK)
         or 204 (No Content) response codes SHOULD be sent to indicate successful 
         completion of the request. */
        try {
            int status = presentityDAO.update(presentity, etag, domain, event, userName);
            logger.debug("Return status {}.", status);
            if (status == 0) {
                return Response.status(204).entity("No record updated.").build();
            }
        } catch (Exception e) {
            logger.error("Error while sending request to database", e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
        return Response.ok().build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllPresentity(@QueryParam("expires") Integer expires) {

        try {
             logger.debug("sfsdfsd");
            presentityDAO = new PresentityDAO();
            List<Presentity> presentityList;
            GenericEntity< List< Presentity>> entity;
            presentityList = presentityDAO.fetchAll(expires);

            ResponseBuilder response;

            if (!presentityList.isEmpty()) {
                entity = new GenericEntity<List<Presentity>>(presentityList) {
                };
                response = Response.ok(entity);
            } else {
                response = Response.status(Response.Status.NOT_FOUND).entity("No resources available.");
            }

            return response.build();
        } catch (Exception e) {
            logger.error("Error while fetching all Presentities.", e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @GET
    @Path("/{presentityId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPresentity(@PathParam("presentityId") String presentityId, @QueryParam("event") String event, @QueryParam("etag") String etag) {
        logger.debug("Query for: " + presentityId + " : " + event + " : " + etag);
        try {
            userName = presentityId.substring(0, presentityId.indexOf('@'));
            domain = presentityId.substring(presentityId.indexOf('@') + 1);
            presentityDAO = new PresentityDAO();
            List<Presentity> presentityList;
            GenericEntity< List< Presentity>> entity;
            presentityList = presentityDAO.findByKey(domain, userName, event, etag);

            ResponseBuilder response;

            if (!presentityList.isEmpty()) {
                entity = new GenericEntity<List<Presentity>>(presentityList) {
                };
                response = Response.ok(entity);
            } else {
                response = Response.status(Response.Status.NOT_FOUND).entity("Requested resource could not be found.");
            }

            return response.build();
        } catch (Exception e) {
            logger.error("Error while fetching presentity {}.", presentityId, e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @DELETE
    @Path("/{presentityId}")
    public Response deletePresentity(@PathParam("presentityId") String presentityId, @QueryParam("event") String event, @QueryParam("etag") String etag) {
        logger.debug("Delete request for: " + presentityId + " : " + event + " : " + etag);

        int status = 0;
        try {
            userName = presentityId.substring(0, presentityId.indexOf('@'));
            domain = presentityId.substring(presentityId.indexOf('@') + 1);
            presentityDAO = new PresentityDAO();
            status = presentityDAO.delete(domain, userName, event, etag);
            ResponseBuilder response;
//Return 200 if any record deleted, 204 otherwise.            
            response = (status > 0 ? Response.ok() : Response.status(204));
            return response.build();
        } catch (Exception e) {
            logger.error("Error while deleting presentity {}.", presentityId, e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @DELETE
    public Response deletePresentityByExpire(@QueryParam("expires") Integer expires) {
        logger.debug("Delete request for: " + expires);

        int status = 0;
        try {
            if (expires != null) {
                presentityDAO = new PresentityDAO();
                status = presentityDAO.deleteByExpires(expires);
            }
            ResponseBuilder response;
//Return 200 if any record deleted, 204 otherwise.            
            response = (status > 0 ? Response.ok() : Response.status(204));
            return response.build();
        } catch (Exception e) {
            logger.error("Error while fetching presentities.", e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @HEAD
    @Path("/{presentityId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkPresentity(@PathParam("presentityId") String presentityId, @QueryParam("event") String event, @QueryParam("etag") String etag) {
        logger.debug("Request for presentity:{}, event: {}, etag: {}", presentityId, event, etag);
        try {
            userName = presentityId.substring(0, presentityId.indexOf('@'));
            domain = presentityId.substring(presentityId.indexOf('@') + 1);
            presentityDAO = new PresentityDAO();
            Boolean recordPresent;
            recordPresent = presentityDAO.check(domain, userName, event, etag);
            ResponseBuilder response;
            if (recordPresent) {
                response = Response.ok();
            } else {
                response = Response.status(Response.Status.NOT_FOUND).entity("Requested resource could not be found.");
            }
            return response.build();
        } catch (Exception e) {
            logger.error("Error while fetching presentity {}.", presentityId, e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @GET
    @Path("/test")
    public String hello() {
        return "Hello, Working";
    }

}
