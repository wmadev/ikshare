package ikshare.server.configuration;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
                config = new ServerConfiguration();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder parser = factory.newDocumentBuilder();
                // TODO: If document is not valid return default
                Document doc = parser.parse(configFile);
                loadServerSettings(doc,config);
            }
        }catch (Exception e) {
            e.printStackTrace();
            config = new DefaultServerConfiguration();
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
            if (child.getNodeName().equals("DatabaseType")){
                config.setDatabaseType(((Element) child).getTextContent());
            }else if(child.getNodeName().equals("DatabaseDriver")){
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
    
    /*public void saveConfiguration(){
        saveConfiguration(config);
    }
    
    public void saveConfiguration(ServerConfiguration config) {
         try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document doc = parser.newDocument();
            Element configurationNode = doc.createElement("ikshare-configuration");
            Element userSettingsNode = buildNetworkSettingsNode(doc);
            configurationNode.appendChild(userSettingsNode);
            doc.appendChild(configurationNode);
            System.out.println("resources"+System.getProperty("file.separator")+"config"+System.getProperty("file.separator")+"server_configuration.xml");
            FileOutputStream out = new FileOutputStream("resources"+System.getProperty("file.separator")+"config"+System.getProperty("file.separator")+"server_configuration.xml");
          
                
            
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(out);
            transformer.setOutputProperty("indent","yes");
            transformer.transform(source,result);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private Element buildNetworkSettingsNode(Document doc){
        return null;
    }*/
        
}
