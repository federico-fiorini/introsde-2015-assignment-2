package rest.lifestyle.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

public class PersonHistoryClient {

	private final transient Client client;
	private final transient WebTarget baseTarget;

	/**
	 * Class constructor
	 * @param serviceUrl
	 */
	public PersonHistoryClient(URI serviceUrl) {
		ClientConfig config = new ClientConfig().register(new JacksonFeature());
        client = ClientBuilder.newClient(config);
        baseTarget = client.target(serviceUrl);
	}


	/**
	 * Get person measure history in XML format
	 * @param personId
	 * @return
	 */
	public Response getPersonMeasureHistoryXml(String personId, String measureType) {
		WebTarget target = baseTarget
				.path("person")
				.path(personId)
				.path(measureType);

		return fetchGetResponse(target, MediaType.APPLICATION_XML);
	}

	/**
	 * Get person measure history in JSON format
	 * @param personId
	 * @return
	 */
	public Response getPersonMeasureHistoryJson(String personId, String measureType) {
		WebTarget target = baseTarget
				.path("person")
				.path(personId)
				.path(measureType);

		return fetchGetResponse(target, MediaType.APPLICATION_JSON);
	}
	
	/**
	 * Get person measure by id in XML format
	 * @param personId
	 * @return
	 */
	public Response getPersonMeasureXml(String personId, String measureType, String mid) {
		WebTarget target = baseTarget
				.path("person")
				.path(personId)
				.path(measureType)
				.path(mid);

		return fetchGetResponse(target, MediaType.APPLICATION_XML);
	}

	/**
	 * Get person measure by id in JSON format
	 * @param personId
	 * @return
	 */
	public Response getPersonMeasureJson(String personId, String measureType, String mid) {
		WebTarget target = baseTarget
				.path("person")
				.path(personId)
				.path(measureType)
				.path(mid);

		return fetchGetResponse(target, MediaType.APPLICATION_JSON);
	}
	
	/**
	 * Get person measure history in XML format
	 * @param personId
	 * @return
	 */
	public Response newPersonMeasureXml(String personId, String measureType, String body) {
		WebTarget target = baseTarget
				.path("person")
				.path(personId)
				.path(measureType);

		return fetchPostResponse(target, body, MediaType.APPLICATION_XML);
	}

	/**
	 * Get person measure history in JSON format
	 * @param personId
	 * @return
	 */
	public Response newPersonMeasureJson(String personId, String measureType, String body) {
		WebTarget target = baseTarget
				.path("person")
				.path(personId)
				.path(measureType);

		return fetchPostResponse(target, body, MediaType.APPLICATION_JSON);
	}

	/**
	 * 
	 * @param target
	 * @param mediaType
	 * @return
	 */
	private Response fetchGetResponse(final WebTarget target, String mediaType) {
		return target.request().accept(mediaType).get();
	}
	
	/**
	 * 
	 * @param target
	 * @param mediaType
	 * @return
	 */
	private Response fetchPostResponse(final WebTarget target, String body, String mediaType) {
		return target.request().accept(mediaType).post(Entity.entity(body, mediaType));
	}
}
