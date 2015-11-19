package rest.lifestyle.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import rest.lifestyle.model.Person;
import rest.lifestyle.client.util.StringFormatter;
import rest.lifestyle.client.util.XmlParser;
import rest.lifestyle.client.util.JsonParser;

public class ClientApp {

	static StringFormatter stringFormatter = new StringFormatter();
	static XmlParser xmlParser = new XmlParser();
	static JsonParser jsonParser = new JsonParser();

	static int reqNumber;
	static String method;
	static String url;
	static String content;
	static String accept;
	static Response response;
	static String bodyXml;
	static String bodyJson;
	static boolean isValid;
	
	public static ArrayList<Integer> successStatus = new ArrayList<Integer>();

	public static Class<String> stringClass = String.class;
	public static Class<Person> personClass = Person.class;
	public static GenericType<List<Person>> personListClass = new GenericType<List<Person>>() {};

	public static PersonClient personClient = new PersonClient(getBaseURI());

	public static void main(String[] args) {
		
		System.out.println("============================================================");
		System.out.println("Server base URL: " + getBaseURI());
        
		//#################
		//#  REQUEST 1    
		//#################
		
		// Set parameters
		reqNumber = 1;
		method = "GET";
		url = "/person";
		content = "";
		successStatus.clear();
		successStatus.add(200);

		// XML
		accept = "application/xml";
		response = personClient.getAllPeopleXml();
		bodyXml = fetchObjectFromResponse(response, stringClass);

		// Check extra requirements
		xmlParser.loadXML(bodyXml);
		NodeList peopleIds = xmlParser.getNodes("//person/id");
		isValid = peopleIds.getLength() > 2;
		
		// Save first and last person IDs
		String firstId = peopleIds.item(0).getTextContent();
		String lastId = peopleIds.item(peopleIds.getLength() - 1).getTextContent();

		printResponseOutput(response, reqNumber, method, url, accept, isValid,
			content, stringFormatter.formatXml(bodyXml));
		
		// JSON
		accept = "application/json";
		response = personClient.getAllPeopleJson();
		bodyJson = fetchObjectFromResponse(response, stringClass);
		printResponseOutput(response, reqNumber, method, url, accept, isValid,
			content, stringFormatter.formatJson(bodyJson));
		
		
		//#################
		//#  REQUEST 2
		//#################
		
		// Set parameters
		reqNumber = 2;
		method = "GET";
		url = "/person/" + firstId;
		content = "";
		successStatus.clear();
		successStatus.add(200);
		successStatus.add(202);

		// XML
		accept = "application/xml";
		response = personClient.getPersonXml(Integer.parseInt(firstId));
		bodyXml = fetchObjectFromResponse(response, stringClass);
		printResponseOutput(response, reqNumber, method, url, accept, true,
				content, stringFormatter.formatXml(bodyXml));
		
		// JSON
		accept = "application/json";
		response = personClient.getPersonJson(Integer.parseInt(firstId));
		bodyJson = fetchObjectFromResponse(response, stringClass);
		printResponseOutput(response, reqNumber, method, url, accept, true,
			content, stringFormatter.formatJson(bodyJson));
		
		
		//#################
		//#  REQUEST 3
		//#################
		
		// Set parameters
		reqNumber = 3;
		method = "PUT";
		url = "/person/" + firstId;
		successStatus.clear();
		successStatus.add(200);
		successStatus.add(201);

		// XML
		accept = "application/xml";
		content = "application/xml";

		// Change first person name
		xmlParser.loadXML(bodyXml);
		String personName = xmlParser.getNodes("//person/firstname").item(0).getTextContent();
		String newName = "NEW NAME XML";
		bodyXml = bodyXml.replace(personName, newName);
		
		// Update request and check if the name has changed
		response = personClient.putPersonXml(Integer.parseInt(firstId), bodyXml);
		bodyXml = fetchObjectFromResponse(response, stringClass);
		xmlParser.loadXML(bodyXml);
		personName = xmlParser.getNodes("//person/firstname").item(0).getTextContent();
		isValid = personName.equals(newName);
		printResponseOutput(response, reqNumber, method, url, accept, isValid,
				content, stringFormatter.formatXml(bodyXml));
		
		// JSON
		accept = "application/json";
		content = "application/json";

		// Change first person name
		jsonParser.loadJson(bodyJson);
		personName = jsonParser.getElement("firstname");
		newName = "NEW NAME JSON";
		bodyJson = bodyJson.replace(personName, newName);
		
		// Update request and check if the name has changed
		response = personClient.putPersonJson(Integer.parseInt(firstId), bodyJson);
		bodyJson = fetchObjectFromResponse(response, stringClass);
		jsonParser.loadJson(bodyJson);
		personName = jsonParser.getElement("firstname");
		isValid = personName.equals(newName);
		printResponseOutput(response, reqNumber, method, url, accept, isValid,
				content, stringFormatter.formatJson(bodyJson));
		
		
		//#################
		//#  REQUEST 4
		//#################
		
		// Set parameters
		reqNumber = 4;
		method = "POST";
		url = "/person";
		successStatus.clear();
		successStatus.add(200);
		successStatus.add(201);
		successStatus.add(202);
		
		// XML
		accept = "application/xml";
		content = "application/xml";
		String xmlPerson = "<person>"
			+ "<firstname>Chuck</firstname>"
			+ "<lastname>Norris</lastname>"
			+ "<birthdate>1945-01-01</birthdate>"
			+ "<healthProfile>"
	        + "<measure>"
	        + "<type>weight</type>"
	        + "<value>78.9</value>"
	        + "</measure>"
	        + "<measure>"
	        + "<type>height</type>"
	        + "<value>172</value>"
	        + "</measure>"
	        + "</healthProfile>"
		    + "</person>";
		
		response = personClient.postNewPersonXml(xmlPerson);
		bodyXml = fetchObjectFromResponse(response, stringClass);
		xmlParser.loadXML(bodyXml);
		
		NodeList idNode = xmlParser.getNodes("//person/id");
		int length = idNode.getLength();
		isValid = false;

		if (length == 1) {
			String personId = idNode.item(0).getTextContent();
			if (!personId.equals("")) 
				isValid = true;
		} 
		
		printResponseOutput(response, reqNumber, method, url, accept, isValid,
				content, stringFormatter.formatXml(bodyXml));
		
		// JSON
		accept = "application/json";
		content = "application/json";
		String jsonPerson = "{"
			+ "\"firstname\": \"Chuck\","
			+ "\"lastname\": \"Norris\","
			+ "\"birthdate\": \"1945-01-01\","
			+ "\"measure\": [ {"
	        + "\"type\": \"weight\","
	        + "\"value\" : \"78.9\""
	        + "}, {"
	        + "\"type\": \"height\","
	        + "\"value\" : \"172\""
	        + "} ]"
	        + "}";
		
		response = personClient.postNewPersonJson(jsonPerson);
		bodyJson = fetchObjectFromResponse(response, stringClass);
		jsonParser.loadJson(bodyJson);
		String personId = jsonParser.getElement("id");
		isValid = !personId.equals("");
		printResponseOutput(response, reqNumber, method, url, accept, isValid,
				content, stringFormatter.formatJson(bodyJson));
    }

    /**
     * 
     * @param response
     * @param reqNumber
     * @param method
     * @param url
     * @param accept
     * @param content
     */
    private static void printResponseOutput(Response response, int reqNumber, String method,
    		String url, String accept, boolean isValid, String content, String body) {
    	
    	System.out.println("============================================================");
		System.out.print("Request #" + reqNumber + ": " + method + " " + url + " Accept: " + accept);
		if (content != "") System.out.print(" Content-Type: " + content);

		int status = response.getStatus();
		System.out.println("\n=> Result: " + isResponseValid(status, isValid));
		System.out.println("=> HTTP Status: " + response.getStatus());
		System.out.println(body);
    }

    /**
     * 
     * @param response
     * @param returnType
     * @return
     */
    private static <T> T fetchObjectFromResponse(Response response, final Class<T> returnType) {
		return response.readEntity(returnType);
	}

    /**
     * 
     * @param response
     * @param returnType
     * @return
     */
	private static <T> List<T> fetchListFromResponse(Response response, GenericType<List<T>> returnType) {
		return response.readEntity(returnType);
	}

	/**
	 * Helper method to return response validness
	 * according to status and extra requirements
	 *
	 * @param response
	 * @return
	 */
	private static String isResponseValid(int status, boolean isValid) {
		return (isValid & successStatus.contains(status)) ? "OK" : "ERROR";
	}

	/**
	 * 
	 * @return
	 */
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://192.168.0.103:5700/rest").build();
    }
}
