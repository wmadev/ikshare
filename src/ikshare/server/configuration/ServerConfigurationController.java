package ikshare.server.configuration;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class ServerConfigurationController {

    private static ServerConfigurationController instance;
    private ServerConfiguration config;
    
    private ServerConfigurationController(){
        loadConfiguration();
    }
    
    public static ServerConfigurationController getInstance(){
        if(instance == null){
            instance = new ServerConfigurationController();
        }
        return instance;
    }

    public void loadConfiguration() {
      try {
            // Try to open config file
            File configFile =  new File("resources"+System.getProperty("file.separator")+"config"+System.getProperty("file.separator")+"server_configuration.xml");
            if(!new File("resources"+System.getProperty("file.separator")+"config").exists())
                new File("resources"+System.getProperty("file.separator")+"config").mkdir();
            
            // if the config file is not found, return default configuration
            if(!configFile.exists()){
                config = new DefaultServerConfiguration();
            }
            else{
                if(!validSchema("resources"+System.getProperty("file.separator")+"config"+System.getProperty("file.separator")+"server_configuration.xml","resources"+System.getProperty("file.separator")+"config"+System.getProperty("file.separator")+"server_configuration.xsd" )){
                    config = new DefaultServerConfiguration();
                }
                else{
                    config = new ServerConfiguration();
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder parser = factory.newDocumentBuilder();
                    // TODO: If document is not valid return default
                    Document doc = parser.parse(configFile);
                    loadServerSettings(doc,config);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            config = new DefaultServerConfiguration();
        }
        System.out.println("Configuration loaded:");
        System.out.println("Driver: "+config.getDatabaseDriver());
        System.out.println("Password: "+config.getDatabasePassword());
        System.out.println("URL: "+config.getDatabaseURL());
        System.out.println("User: "+config.getDatabaseUser());
      
    }
    public boolean validSchema(String xmlPath,String schemaPath){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", new File(schemaPath));
            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(new SaxErrorHandler());
            reader.parse(xmlPath);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    
    public ServerConfiguration getConfiguration(){
        return config;
    }

    private void loadServerSettings(Document doc, ServerConfiguration config) {
        Node node = doc.getDocumentElement().getElementsByTagName("server-settings").item(0);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeName().equals("databaseType")){
                config.setDatabaseType(((Element) child).getTextContent());
            }else if(child.getNodeName().equals("databaseDriver")){
                config.setDatabaseDriver(((Element) child).getTextContent());
            }else if(child.getNodeName().equals("databaseUser")){
                config.setDatabaseUser(((Element) child).getTextContent());
            }else if(child.getNodeName().equals("databasePassword")){
                config.setDatabasePassword(((Element)child).getTextContent());
            }else if(child.getNodeName().equals("databaseURL")){
                config.setDatabaseURL(((Element)child).getTextContent());
            }
        }
    }
     class SaxErrorHandler implements ErrorHandler {

	public void error(SAXParseException exception) throws SAXException {
		System.err.println(exception.getMessage());
		throw exception;

	}

	public void fatalError(SAXParseException exception) throws SAXException {
		System.err.println(exception.getMessage());
		throw exception;
	}

	public void warning(SAXParseException exception) throws SAXException {
		System.err.println(exception.getMessage());
		throw exception;
		
	}
     }
}
