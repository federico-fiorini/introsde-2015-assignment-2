package rest.lifestyle.client.util;

import java.io.IOException;
import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlParser {

	Document doc;
	XPath xpath;

	/**
	 * Class constructor
	 */
	public XmlParser() {
		 XPathFactory factory = XPathFactory.newInstance();
	     xpath = factory.newXPath();
	}

	/**
     * Method to load XML file
     *
     * @param xmlFilepath
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public void loadXML(String xml) {

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);

        try {
        	DocumentBuilder builder = domFactory.newDocumentBuilder();
        	InputSource is = new InputSource(new StringReader(xml));
        	doc = builder.parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			doc = null;
			e.printStackTrace();
		}
    }

    /**
     * Evaluate xpath expression and return nodes
     * @param xpathExpr
     * @return
     */
    public NodeList getNodes(String xpathExpr) {
        try {
        	XPathExpression expr = xpath.compile(xpathExpr);
			return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
    }
}
