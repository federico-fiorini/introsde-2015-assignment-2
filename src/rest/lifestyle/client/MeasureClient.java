package rest.lifestyle.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

public class MeasureClient {

	private final transient Client client;
	private final transient WebTarget baseTarget;

	/**
	 * Class constructor
	 * @param serviceUrl
	 */
	public MeasureClient(URI serviceUrl) {
		ClientConfig config = new ClientConfig().register(new JacksonFeature());
        client = ClientBuilder.newClient(config);
        baseTarget = client.target(serviceUrl);
	}

	/**
	 * Get all measure types in XML format
	 * @return
	 */
	public Response getAllMeasuresXml() {
		WebTarget target = baseTarget
				.path("measureTypes");

		return fetchGetResponse(target, MediaType.APPLICATION_XML);
	}

	/**
	 * Get all measure types in JSON format
	 * @return
	 */
	public Response getAllMeasuresJson() {
		WebTarget target = baseTarget
				.path("measureTypes");

		return fetchGetResponse(target, MediaType.APPLICATION_JSON);
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
}
