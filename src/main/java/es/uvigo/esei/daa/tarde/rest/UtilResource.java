package es.uvigo.esei.daa.tarde.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uvigo.esei.daa.tarde.Config;

@Path("/util")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilResource {

    @GET
    @Path("articles_per_page")
    public Response getArticlesPerPage( ) {
        return Response.ok(Config.getInteger("articles_per_page")).build();
    }

}
