package com.presence.services.rest;

import com.presence.dao.PresentityDAO;
import com.presence.model.Presentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
            if (status == 0)
                return Response.status(409).entity("No record inserted.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Server was unable to process the request.").build();
        }
        return Response.ok().build();
    }
    
    @GET
    @Path("/{presentityId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public Response getPresentityByUserAndDomain(@PathParam("presentityId") String presentityId, @QueryParam("event") String event){
    	System.out.println("Query for: "+presentityId + " : "+ event);
    	String userName = presentityId.substring(0, presentityId.indexOf('@'));
		String domain = presentityId.substring(presentityId.indexOf('@') + 1);
    	PresentityDAO presentityDAO = new PresentityDAO();
    	List<Presentity> presentityList ;
    	GenericEntity< List< Presentity > > entity;
    	presentityList = presentityDAO.find(domain, userName, event);
    	
    	Iterator itr = presentityList.iterator();
    	while(itr.hasNext())
    	{
    		System.out.println(itr.next().toString());
    	}
    	entity  = new GenericEntity<List<Presentity>>( presentityList ) { };
    	
    	ResponseBuilder response = Response.ok(entity);
    	
		return response.build();
    	
    }
    
@GET
@Path("/test")
public String hello(){
	return "Hello, Working";
}
}
