/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ikshare.server.configuration;

import java.io.File;
import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author awosy
 */
public class ServerConfigurationController {

    private static ServerConfigurationController instance;
    private ServerConfiguration config;
    
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
                //loadUserSettings(doc,config);
            }
        }catch (Exception e) {
            e.printStackTrace();
            config = new DefaultServerConfiguration();
        }
    }
    public ServerConfiguration getConfiguration(){
        return config;
    }public void saveConfiguration(){
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
    }
    
    
    
    
    
}
