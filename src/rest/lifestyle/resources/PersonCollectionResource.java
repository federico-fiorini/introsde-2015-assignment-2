package rest.lifestyle.resources;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import rest.lifestyle.model.Person;

@Path("/person")
public class PersonCollectionResource {

    @Context
    UriInfo uriInfo;

    @Context
    Request request;

    @GET
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public List<Person> getPersons() {
        System.out.println("Getting list of people...");
        List<Person> people = Person.getAll();
        return people;
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response newPerson(Person person) throws IOException {

        System.out.println("Creating new person...");            
        person = Person.savePerson(person);
        
        URI location = UriBuilder.fromUri(uriInfo.getAbsolutePath())
        	.path(Integer.toString(person.getId()))
        	.build();
        return Response.created(location).entity(person).build();     
    }

    @Path("{personId}")
    public PersonResource getPerson(@PathParam("personId") int id) {
        return new PersonResource(uriInfo, request, id);
    }
}
