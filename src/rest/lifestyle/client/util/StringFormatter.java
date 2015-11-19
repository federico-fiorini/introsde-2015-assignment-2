package rest.lifestyle.client.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringFormatter {

	/**
     * Format XML with indentation
     *
     * @param unformattedXml
     * @return
     */
    public String formatXml(String unformattedXml) {
    	Transformer transformer;
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e1) {
			return unformattedXml;
		}

    	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    	transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
    	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    	
    	StreamResult result = new StreamResult(new StringWriter());
    	StreamSource source = new StreamSource(new StringReader(unformattedXml));
    	
    	try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			return unformattedXml;
		}

    	return result.getWriter().toString();
    }

    /**
     * Format JSON with indentation
     *
     * @param unformattedJson
     * @return
     */
    public String formatJson(String unformattedJson) {
    	ObjectMapper mapper = new ObjectMapper();
    	Object json = new Object();

    	try {
			json = mapper.readValue(unformattedJson, Object.class);
		} catch (IOException e) {
			return unformattedJson;
		}
    	
    	try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (JsonProcessingException e) {
			return unformattedJson;
		}
    }
}
