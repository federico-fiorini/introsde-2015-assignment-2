package rest.lifestyle.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.w3c.dom.NodeList;

import rest.lifestyle.client.util.StringFormatter;
import rest.lifestyle.client.util.XmlParser;
import rest.lifestyle.client.util.JsonParser;

public class ClientApp {

	static StringFormatter stringFormatter = new StringFormatter();
	static XmlParser xmlParser = new XmlParser();
	static JsonParser jsonParser = new JsonParser();
	
	static Random randomGenerator = new Random();

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

	public static PersonClient personClient = new PersonClient(getBaseURI());
	public static MeasureClient measureClient = new MeasureClient(getBaseURI());
	public static PersonHistoryClient personHistoryClient = new PersonHistoryClient(getBaseURI());

	public static void main(String[] args) throws Exception {
		
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

		// Check requirements
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
		
		
		//#################
		//#  REQUEST 5
		//#################
		
		// Set parameters
		reqNumber = 5;
		method = "DELETE";
		url = "/person/" + personId;
		accept = "";
		content = "";
		successStatus.clear();
		successStatus.add(200);
		successStatus.add(204);

		// Delete person and check if has been deleted
		response = personClient.deletePerson(Integer.parseInt(personId));
		Response testGetResponse = personClient.getPersonJson(Integer.parseInt(personId));
		isValid = testGetResponse.getStatus() == 404;
		printResponseOutput(response, reqNumber, method, url, accept, isValid,
				content, "");

		
		//#################
		//#  REQUEST 6
		//#################
		
		// Set parameters
		reqNumber = 6;
		method = "GET";
		url = "/measureTypes";
		content = "";
		successStatus.clear();
		successStatus.add(200);
		
		// XML
		accept = "application/xml";
		response = measureClient.getAllMeasuresXml();
		bodyXml = fetchObjectFromResponse(response, stringClass);
		xmlParser.loadXML(bodyXml);
		
		NodeList measures = xmlParser.getNodes("//measureType");
		isValid = measures.getLength() > 2;
		
		List<String> measuresType = new ArrayList<String>();
		
		for (int i = 0; i < measures.getLength(); i++) {
			measuresType.add(measures.item(i).getTextContent());
		}

		printResponseOutput(response, reqNumber, method, url, accept, isValid,
			content, stringFormatter.formatXml(bodyXml));
		
		// JSON
		accept = "application/json";
		response = measureClient.getAllMeasuresJson();
		bodyJson = fetchObjectFromResponse(response, stringClass);
		printResponseOutput(response, reqNumber, method, url, accept, isValid,
				content, stringFormatter.formatJson(bodyJson));
		
		
		//#################
		//#  REQUEST 7
		//#################
		
		// Set parameters
		reqNumber = 7;
		method = "GET";
		content = "";
		successStatus.clear();
		successStatus.add(200);
		String[] persons = {firstId, lastId};
		String measureTypeStored = "";
		String measureIdStored = "";
		String personIdStored = "";
		
		// For first and last person
		for (String id : persons) {
			// For each measure type
			for (String measureType : measuresType) {

				// Get request in XML and JSON
				url = "/person/" + id + "/" + measureType;
				
				// XML
				accept = "application/xml";
				response = personHistoryClient.getPersonMeasureHistoryXml(id, measureType);
				bodyXml = fetchObjectFromResponse(response, stringClass);
				xmlParser.loadXML(bodyXml);
				NodeList history = xmlParser.getNodes("//measure/mid");
				isValid = history.getLength() > 0;
				
				// Store one measure id
				if (isValid) {
					measureTypeStored = measureType;
					measureIdStored = history.item(0).getTextContent();
					personIdStored = id;
				}

				printResponseOutput(response, reqNumber, method, url, accept, isValid,
						content, stringFormatter.formatXml(bodyXml));
				
				// JSON
				accept = "application/json";
				response = personHistoryClient.getPersonMeasureHistoryJson(id, measureType);
				bodyJson = fetchObjectFromResponse(response, stringClass);
				printResponseOutput(response, reqNumber, method, url, accept, isValid,
						content, stringFormatter.formatJson(bodyJson));
			}
		}
		

		//#################
		//#  REQUEST 8
		//#################
		
		// Set parameters
		reqNumber = 8;
		method = "GET";
		content = "";
		successStatus.clear();
		successStatus.add(200);
		url = "/person/" + personIdStored + "/" + measureTypeStored + "/" + measureIdStored;

		// XML
		accept = "application/xml";
		response = personHistoryClient.getPersonMeasureXml(personIdStored, measureTypeStored, measureIdStored);
		bodyXml = fetchObjectFromResponse(response, stringClass);
		printResponseOutput(response, reqNumber, method, url, accept, true,
				content, stringFormatter.formatXml(bodyXml));

		// JSON
		accept = "application/json";
		response = personHistoryClient.getPersonMeasureJson(personIdStored, measureTypeStored, measureIdStored);
		bodyJson = fetchObjectFromResponse(response, stringClass);
		printResponseOutput(response, reqNumber, method, url, accept, true,
				content, stringFormatter.formatJson(bodyJson));

		
		//#################
		//#  REQUEST 9
		//#################
		
		// Set parameters
		reqNumber = 9;
		method = "GET";
		content = "";
		successStatus.clear();
		successStatus.add(200);
		
		// Get a random measure
		int i = randomGenerator.nextInt(measuresType.size());
		String randomMeasure = measuresType.get(i);
		url = "/person/" + firstId + "/" + randomMeasure;
		
		// XML
		accept = "application/xml";
		response = personHistoryClient.getPersonMeasureHistoryXml(firstId, randomMeasure);
		bodyXml = fetchObjectFromResponse(response, stringClass);
		
		// Save count of measurements
		xmlParser.loadXML(bodyXml);
		NodeList history = xmlParser.getNodes("//measure/mid");
		int count = history.getLength();
		
		printResponseOutput(response, reqNumber, method, url, accept, true,
				content, stringFormatter.formatXml(bodyXml));
		
		// POST new measurement
		method = "POST";
		content = "application/xml";
		successStatus.add(201);
		successStatus.add(204);
		String xmlMeasure = "<measure>"
            + "<value>72</value>"
            + "<created>2011-12-09</created>"
            + "</measure>";
		
		response = personHistoryClient.newPersonMeasureXml(firstId, randomMeasure, xmlMeasure);
		bodyXml = fetchObjectFromResponse(response, stringClass);
		printResponseOutput(response, reqNumber, method, url, accept, true,
				content, stringFormatter.formatXml(bodyXml));
		
		// Check if measurements are incremented by 1
		method = "GET";
		content = "";
		successStatus.clear();
		successStatus.add(200);
		
		response = personHistoryClient.getPersonMeasureHistoryXml(firstId, randomMeasure);
		bodyXml = fetchObjectFromResponse(response, stringClass);
		
		xmlParser.loadXML(bodyXml);
		history = xmlParser.getNodes("//measure/mid");
		isValid = history.getLength() == count + 1;
		
		printResponseOutput(response, reqNumber, method, url, accept, isValid,
				content, stringFormatter.formatXml(bodyXml));
		
		// JSON
		accept = "application/json";
		response = personHistoryClient.getPersonMeasureHistoryJson(firstId, randomMeasure);
		bodyJson = fetchObjectFromResponse(response, stringClass);
		
		// Save count of measurements
		jsonParser.loadJson(bodyJson);
		int countJson = jsonParser.countList();
		
		printResponseOutput(response, reqNumber, method, url, accept, true,
				content, stringFormatter.formatJson(bodyJson));
		
		// POST new measurement
		method = "POST";
		content = "application/json";
		successStatus.add(201);
		successStatus.add(204);
		String jsonMeasure = "{"
            + "\"value\" : \"72\","
            + "\"created\" : \"2011-12-09\""
            + "}";
		
		response = personHistoryClient.newPersonMeasureJson(firstId, randomMeasure, jsonMeasure);
		bodyJson = fetchObjectFromResponse(response, stringClass);
		printResponseOutput(response, reqNumber, method, url, accept, true,
				content, stringFormatter.formatJson(bodyJson));
		
		// Check if measurements are incremented by 1
		method = "GET";
		content = "";
		successStatus.clear();
		successStatus.add(200);
		
		response = personHistoryClient.getPersonMeasureHistoryJson(firstId, randomMeasure);
		bodyJson = fetchObjectFromResponse(response, stringClass);
		
		jsonParser.loadJson(bodyJson);
		isValid = jsonParser.countList() == countJson + 1;
		
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
		System.out.print("Request #" + reqNumber + ": " + method + " " + url);
		if (accept != "") System.out.print(" Accept: " + accept);
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
        return UriBuilder.fromUri("http://10.198.201.207:5700/rest").build();
    }
}
