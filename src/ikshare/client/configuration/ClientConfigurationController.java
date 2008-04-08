package ikshare.client.configuration;

import ikshare.domain.event.EventController;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ClientConfigurationController {
    private static ClientConfigurationController instance;
    private ClientConfiguration config;
    
    private ClientConfigurationController() {
        loadConfiguration();
    }
    
    public static ClientConfigurationController getInstance(){
        if(instance == null){
            instance = new ClientConfigurationController();
        }
        return instance;
    }

    public void changeLanguage(String language) {
        config.setLanguage(language);
    }
    
    public void loadConfiguration() {
        try {
            // Try to open config file
            String sep = System.getProperty("file.separator");
            File configFile =  new File(System.getProperty("user.home")+sep+"ikshare"+sep+"configuration.xml");
            if(!new File(System.getProperty("user.home")+sep+"ikshare").exists()){
                new File(System.getProperty("user.home")+sep+"ikshare").mkdir();
            }
            // if the config file is not found, return default configuration
            if(!configFile.exists()){
                config = new DefaultClientConfiguration();
            }
            else{
                config = new ClientConfiguration();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder parser = factory.newDocumentBuilder();
                // TODO: If document is not valid return default
                Document doc = parser.parse(configFile);
                loadUserSettings(doc,config);
                loadNetworkSettings(doc,config);
            }
        }catch (Exception e) {
            e.printStackTrace();
            config = new DefaultClientConfiguration();
        } 
    }
        
    private void loadUserSettings(Document doc,ClientConfiguration config){
        Node node = doc.getDocumentElement().getElementsByTagName("user-settings").item(0);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeName().equals("language")){
                config.setLanguage(((Element) child).getTextContent());
            }else if(child.getNodeName().equals("birthday")){
                StringTokenizer tokenizer = new StringTokenizer(((Element) child).getTextContent(),"-");
                Calendar date = Calendar.getInstance();
                date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tokenizer.nextToken()));
                date.set(Calendar.MONTH, Integer.parseInt(tokenizer.nextToken())-1);
                date.set(Calendar.YEAR, Integer.parseInt(tokenizer.nextToken()));
                config.setBirthday(date);
            }else if(child.getNodeName().equals("nickname")){
                config.setNickname(((Element) child).getTextContent());
            }else if(child.getNodeName().equals("shared-folder")){
                config.setSharedFolder(new File(((Element)child).getTextContent()));
            }
        }
    }
    private void loadNetworkSettings(Document doc,ClientConfiguration config){
        Node node = doc.getDocumentElement().getElementsByTagName("network-settings").item(0);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeName().equals("ikshare-server-address")){
                config.setIkshareServerAddress(((Element) child).getTextContent());
            }else if(child.getNodeName().equals("ikshare-server-port")){
                try{
                    config.setIkshareServerPort(Integer.parseInt(((Element) child).getTextContent()));
                }
                catch(NumberFormatException e){
                    config.setIkshareServerPort(6000);
                }
            }else if(child.getNodeName().equals("file-transfer-port")){
                try{
                    config.setFileTransferPort(Integer.parseInt(((Element) child).getTextContent()));
                }
                catch(NumberFormatException e){
                    config.setFileTransferPort(6666);
                }
            }
        }
    }
    
    public void saveConfiguration(){
        saveConfiguration(config);
    }
    
    public void saveConfiguration(ClientConfiguration config) {
         try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document doc = parser.newDocument();
            Element configurationNode = doc.createElement("ikshare-configuration");
            Element userSettingsNode = buildUserSettingsNode(doc);
            Element networkSettingsNode = buildNetworkSettingsNode(doc);
            configurationNode.appendChild(userSettingsNode);
            configurationNode.appendChild(networkSettingsNode);
            doc.appendChild(configurationNode);
            FileOutputStream out = new FileOutputStream(System.getProperty("user.home")+System.getProperty("file.separator")+"ikshare"+System.getProperty("file.separator")+"configuration.xml");
          
             
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
        // Language
        Element language = doc.createElement("language");
        language.appendChild(doc.createTextNode(config.getLanguage()));
        
        // birthday
        Element birthday = doc.createElement("birthday");
        birthday.appendChild(doc.createTextNode(config.getBirthday().get(Calendar.DAY_OF_MONTH)+
                "-"+(config.getBirthday().get(Calendar.MONTH)+1)+"-"+config.getBirthday().get(Calendar.YEAR)));
        
        // nickname
        Element nickname = doc.createElement("nickname");
        nickname.appendChild(doc.createTextNode(config.getNickname()));
        // shared folder
        Element sharedFolder = doc.createElement("shared-folder");
        sharedFolder.appendChild(doc.createTextNode(config.getSharedFolder().getAbsolutePath()));
        // add to usersettings
        userSettings.appendChild(nickname);
        userSettings.appendChild(language);
        userSettings.appendChild(birthday);
        userSettings.appendChild(sharedFolder);
        return userSettings;
    }
    private Element buildNetworkSettingsNode(Document doc){
        Element networkSettings = doc.createElement("network-settings");
        // Ikshare server address
        Element serverAddress = doc.createElement("ikshare-server-address");
        serverAddress.appendChild(doc.createTextNode(config.getIkshareServerAddress()));
        
        // Ikshare server port
        Element serverPort = doc.createElement("ikshare-server-port");
        serverPort.appendChild(doc.createTextNode(String.valueOf(config.getIkshareServerPort())));
        
        // File transfer port
        Element transferPort = doc.createElement("file-transfer-port");
        transferPort.appendChild(doc.createTextNode(String.valueOf(config.getFileTransferPort())));
        
        // add to networksettings
        networkSettings.appendChild(serverAddress);
        networkSettings.appendChild(serverPort);
        networkSettings.appendChild(transferPort);
        return networkSettings;
    }
    
    public String getString(String key){
        try{
            return ResourceBundle.getBundle("ikshare.client.gui.bundles.messages",new Locale(config.getLanguage())).getString(key);
        }
        catch(MissingResourceException e){
            return "-- KeyNotFound --";
        }
    }
    public ClientConfiguration getConfiguration(){
        return config;
    }
}
