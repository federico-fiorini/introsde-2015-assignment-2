package rest.lifestyle.resources;

import rest.lifestyle.model.Person;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class PersonResource {

    @Context
    UriInfo uriInfo;

    @Context
    Request request;

    int id;

    /**
     * Class constructor
     * @param uriInfo
     * @param request
     * @param id
     */
    public PersonResource(UriInfo uriInfo, Request request, int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public Person getPerson() {
        Person person = getPersonById(this.id);
        
        if (person == null) {
        	System.out.println("[Error] Get: Person with id " + this.id + " not found");
            throw new NotFoundException();
        }
        
        System.out.println("Returning person  " + person.getId());
        return person;
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response putPerson(Person person) {

        Person existing = getPersonById(this.id);

        if (existing == null) {
        	throw new NotFoundException();
        }
        
        System.out.println("Updating person " + this.id);

        person = existing.updatePerson(person);
        return Response.status(200).entity(person).build();        
    }

    @DELETE
    public void deletePerson() {
    	
        Person person = getPersonById(this.id);

        if (person == null) {
        	System.out.println("[Error] Delete: Person with id " + this.id + " not found");
            throw new NotFoundException();
        }
        
        System.out.println("Deleting person " + this.id);
        Person.deletePerson(person);
    }

    /**
     * Helper method to find person by id
     * @param personId
     * @return
     */
    public Person getPersonById(int personId) {
        System.out.println("Reading person from DB with id: " + personId);

        return Person.getPersonById(personId);
    }

    @Path("{measureType}")
    public HealthProfileHistoryResource getMeasureType(@PathParam("measureType") String measureType) {
        return new HealthProfileHistoryResource(uriInfo, request, id, measureType);
    }
}