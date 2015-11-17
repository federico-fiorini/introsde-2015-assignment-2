package rest.lifestyle.resources;

import rest.lifestyle.model.HealthProfile;
import rest.lifestyle.model.HealthProfileHistory;
import rest.lifestyle.model.Person;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class HealthProfileHistoryResource {

	@Context
    UriInfo uriInfo;

    @Context
    Request request;

    int id;

    String measureType;

    /**
     * Class constructor
     * @param uriInfo
     * @param request
     * @param id
     * @param measureType
     */
    public HealthProfileHistoryResource(UriInfo uriInfo, Request request, int id, String measureType) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.measureType = measureType;
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public List<HealthProfileHistory> getHistory() {

        Person person = Person.getPersonById(this.id);
        
        if (person == null) {
        	System.out.println("[Error] Get: Person with id " + this.id + " not found");
            throw new NotFoundException();
        }
        
        System.out.println("Returning person " + measureType + " history " + person.getId());
        return HealthProfileHistory.getHistoryByPersonAndMeasureType(person, measureType);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public HealthProfile updateMeasure(HealthProfile newMeasure) {

        Person person = Person.getPersonById(this.id);
        newMeasure.setPerson(person);
        newMeasure.setMeasureType(measureType);
        
        if (person == null) {
        	System.out.println("[Error] Get: Person with id " + this.id + " not found");
            throw new NotFoundException();
        }
        
        System.out.println("Creating/updating measure");

        HealthProfile current = new HealthProfile();
        Integer currentId = null;

        // Get current measure and copy to history
        current = HealthProfile.getMeasureByType(person, measureType);
        if (current != null) {
        	HealthProfileHistory.copyHealthProfileToHistory(current);
        	currentId = (Integer) current.getId();
        }

        // Set new measure and update on DB
        current = HealthProfile.updateHealthProfile(currentId, newMeasure);
    	return current;
    }

    @GET
    @Path("{mid}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public HealthProfileHistory getMeasure(@PathParam("mid") int measureId) {

    	Person person = Person.getPersonById(this.id);
        
        if (person == null) {
        	System.out.println("[Error] Get: Person with id " + this.id + " not found");
            throw new NotFoundException();
        }
        
        System.out.println("Returning measure " + measureId);
        HealthProfileHistory measure = HealthProfileHistory.getMeasureById(person, measureType, measureId);
        if (measure == null) {
        	System.out.println("[Error] Get: Measure with id " + measureId + " not found");
            throw new NotFoundException();
        }

        return measure;
    }
}
