package com.presence.services.rest;

import com.presence.dao.PresentityDAO;
import com.presence.model.Presentity;

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

/**
 * Created by Suryaveer on 7/20/2015.
 */
@Path("/V1/presentity")
public class PresentityService {

    /* This method allows to insert new presentity into the repository */
    @POST
    @Consumes("application/json")
    public Response addPresentity(Presentity presentity) {

        System.out.println(presentity);
        PresentityDAO presentityDAO = new PresentityDAO();
        try {
            int status = presentityDAO.create(presentity);
            if (status == 0) {
                return Response.status(409).entity("No record inserted.").build();
            }
        } catch (Exception e) {
            e.printStackTrace();

            return Response.status(500).entity("Server was unable to process the request.").build();
        }
        return Response.status(201).build();
    }
    /* This method allows to update new presentity into the repository */

    @PUT
    @Path("/{presentityId}")
    @Consumes("application/json")
    public Response updatePresentity(@PathParam("presentityId") String presentityId, Presentity presentity, @QueryParam("event") String event, @QueryParam("etag") String etag) {
        System.out.println("****************************");
        System.out.println(presentity);
        String userName = presentityId.substring(0, presentityId.indexOf('@'));
        String domain = presentityId.substring(presentityId.indexOf('@') + 1);
        PresentityDAO presentityDAO = new PresentityDAO();
        System.out.println(presentity);
        /*RFC2616 If an existing resource is modified, either the 200 (OK)
         or 204 (No Content) response codes SHOULD be sent to indicate successful 
         completion of the request. */
        try {
            int status = presentityDAO.update(presentity, etag, domain, event, userName);
            if (status == 0) {
                return Response.status(204).entity("No record updated.").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
        return Response.ok().build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllPresentity() {

        try {
            PresentityDAO presentityDAO = new PresentityDAO();
            List<Presentity> presentityList;
            GenericEntity< List< Presentity>> entity;
            presentityList = presentityDAO.fetchAll();

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
            e.printStackTrace();
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @GET
    @Path("/{presentityId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPresentityByUserAndDomain(@PathParam("presentityId") String presentityId, @QueryParam("event") String event, @QueryParam("etag") String etag) {
        System.out.println("Query for: " + presentityId + " : " + event + " : " + etag);
        try {
            String userName = presentityId.substring(0, presentityId.indexOf('@'));
            String domain = presentityId.substring(presentityId.indexOf('@') + 1);
            PresentityDAO presentityDAO = new PresentityDAO();
            List<Presentity> presentityList;
            GenericEntity< List< Presentity>> entity;
            presentityList = presentityDAO.find(domain, userName, event, etag);

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
            e.printStackTrace();
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @DELETE
    @Path("/{presentityId}")
    public Response deletePresentity(@PathParam("presentityId") String presentityId, @QueryParam("event") String event, @QueryParam("etag") String etag) {
        System.out.println("Delete request for: " + presentityId + " : " + event + " : " + etag);

        int status = 0;
        try {
            String userName = presentityId.substring(0, presentityId.indexOf('@'));
            String domain = presentityId.substring(presentityId.indexOf('@') + 1);
            PresentityDAO presentityDAO = new PresentityDAO();
            status = presentityDAO.delete(domain, userName, event, etag);
            ResponseBuilder response;
//Return 200 if any record deleted, 204 otherwise.            
            response = (status > 0 ? Response.ok() : Response.status(204));
            return response.build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @HEAD
    @Path("/{presentityId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkPresentity(@PathParam("presentityId") String presentityId, @QueryParam("event") String event, @QueryParam("etag") String etag) {
        System.out.println("Query for: " + presentityId + " : " + event + " : " + etag);
        try {
            String userName = presentityId.substring(0, presentityId.indexOf('@'));
            String domain = presentityId.substring(presentityId.indexOf('@') + 1);
            PresentityDAO presentityDAO = new PresentityDAO();
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
            e.printStackTrace();
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
    }

    @GET
    @Path("/test")
    public String hello() {
        return "Hello, Working";
    }

}
