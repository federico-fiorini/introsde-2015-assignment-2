package rest.lifestyle.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import rest.lifestyle.model.MeasureType;

@Path("/measureTypes")
public class MeasureTypeCollectionResource {

	@Context
    UriInfo uriInfo;

    @Context
    Request request;

    @GET
    @Produces({MediaType.APPLICATION_XML,  MediaType.APPLICATION_JSON ,  MediaType.TEXT_XML })
    public List<MeasureType> getMeasureTypes() {
        System.out.println("Getting list of supported measures");

        List<MeasureType> measures = MeasureType.getAll();
        return measures;
    }
}
