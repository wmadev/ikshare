/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui.configuration;

import ikshare.client.gui.configuration.Configuration;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author awosy
 */
public class ConfigurationController {
    private static ConfigurationController instance;
    private Configuration config;
    
    private ConfigurationController() {
        loadConfiguration();
    }
    
    public static ConfigurationController getInstance(){
        if(instance == null){
            instance = new ConfigurationController();
        }
        return instance;
    }

    public void changeLanguage(String language) {
        config.setLanguage(language);
    }
    
    public void loadConfiguration() {
        try {
            // Try to open config file
            File configFile = new File("resources/config/configuration.xml");
            // if the config file is not found, return default configuration
            if(!configFile.exists()){
                System.out.println("Config not found");
                //configFile.createNewFile();
                config = new DefaultConfiguration();
            }
            else{
                config = new Configuration();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder parser = factory.newDocumentBuilder();
                // TODO: If document is not valid return default
                Document doc = parser.parse(configFile);
                loadUserSettings(doc,config);
            }
        }catch (Exception e) {
            e.printStackTrace();
            config = new DefaultConfiguration();
        } 
    }
        
    private void loadUserSettings(Document doc,Configuration config){
        Node node = doc.getDocumentElement().getElementsByTagName("user-settings").item(0);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeName().equals("language")){
                config.setLanguage(((Element) child).getTextContent());
            }
        }
    }
    
    public void saveConfiguration(){
        saveConfiguration(config);
    }
    
    public void saveConfiguration(Configuration config) {
         try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document doc = parser.newDocument();
            Element configurationNode = doc.createElement("ikshare-configuration");
            Element userSettingsNode = buildUserSettingsNode(doc);
            configurationNode.appendChild(userSettingsNode);
            doc.appendChild(configurationNode);
            FileOutputStream out = new FileOutputStream("resources/config/configuration.xml");
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
    
    private Element buildUserSettingsNode(Document doc){
        Element userSettings = doc.createElement("user-settings");
        Element language = doc.createElement("language");
        language.appendChild(doc.createTextNode(config.getLanguage()));
        userSettings.appendChild(language);
        return userSettings;
    }
    
    public String getString(String key){
        try{
            return ResourceBundle.getBundle("ikshare.client.gui.bundles.messages",new Locale(config.getLanguage())).getString(key);
        }
        catch(MissingResourceException e){
            return "-- KeyNotFound --";
        }
    }
    public Configuration getConfiguration(){
        return config;
    }
}
