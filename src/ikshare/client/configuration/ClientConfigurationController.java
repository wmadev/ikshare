package ikshare.client.configuration;

import ikshare.exceptions.ConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

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
    public int[] getSharedInformation(){
        int[] info = new int[2];
        info[0] = -1;
        info[1] = 0;
        countNumber(info, config.getSharedFolder());
        //System.out.println(getString("shareinfofolder")+info[0]+" "+getString("shareinfofile")+" "+info[1]);
        return info;
    }
    private void countNumber(int[] counter,File f){
    	if (f != null) {
	        if(f.isDirectory()){
	            counter[0]++;
	            for(File sub:f.listFiles()){
	                countNumber(counter, sub);
	            }
	        }
	        else{
	            counter[1]++;
	        }
    	}
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
                if(!validSchema(System.getProperty("user.home")+sep+"ikshare"+sep+"configuration.xml","resources"+sep+"config"+sep+"client_configuration.xsd" )){
                    System.out.println("ongeldige windowsrommel");
                    config = new DefaultClientConfiguration();
                }
                else{
                    System.out.println("geldig");
                    config = new ClientConfiguration();
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder parser = factory.newDocumentBuilder();
                    Document doc = parser.parse(configFile);
                    loadUserSettings(doc,config);
                    loadNetworkSettings(doc,config);
                }
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
            if(child.getNodeName().equals("accountname")){
                config.setAccountName(((Element) child).getTextContent());
            }
            else if (child.getNodeName().equals("language")){
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
            }else if(child.getNodeName().equals("maximum-uploads")){
            	try {
            		config.setMaximumUploads(Integer.parseInt(((Element)child).getTextContent()));
            	} catch (Exception e) {
            		config.setMaximumUploads(10);
            	}
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
            }else if(child.getNodeName().equals("message-port")) {
            	try {
            		config.setMessagePort(6001);
            	} catch (NumberFormatException e) {
            		config.setMessagePort(6001);
            	}
            }else if(child.getNodeName().equals("chat-server-port")) {
            	try {
            		config.setChatServerPort(Integer.parseInt(((Element)child).getTextContent()));
            	}
            	catch (NumberFormatException e){
            		config.setChatServerPort(6005);
            	}
            }else if(child.getNodeName().equals("chat-server-address")) {
            	config.setChatServerAddress(((Element)child).getTextContent());
            }
        }
    }
    
    public boolean validSchema(String xmlPath,String schemaPath){
        System.out.println(schemaPath);
        System.out.println(xmlPath);
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", new File(schemaPath));
            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(new SaxErrorHandler());
            InputSource input = new InputSource(xmlPath);
            reader.parse(input);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
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
        
        Element accountName = doc.createElement("accountname");
        accountName.appendChild(doc.createTextNode(config.getAccountName()));

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
        // max upload
        Element maximumUploads = doc.createElement("maximum-uploads");
        maximumUploads.appendChild(doc.createTextNode(String.valueOf(config.getMaximumUploads())));
        // add to usersettings
        userSettings.appendChild(accountName);
        userSettings.appendChild(nickname);
        userSettings.appendChild(language);
        userSettings.appendChild(birthday);
        userSettings.appendChild(sharedFolder);
        userSettings.appendChild(maximumUploads);
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
        
        Element messagePort = doc.createElement("message-port");
        messagePort.appendChild(doc.createTextNode(String.valueOf(config.getMessagePort())));
        
        // chat server address
        Element chatServerAddress = doc.createElement("chat-server-address");
        chatServerAddress.appendChild(doc.createTextNode(config.getChatServerAddress()));
        
        // chat server port
        Element chatServerPort = doc.createElement("chat-server-port");
        chatServerPort.appendChild(doc.createTextNode(String.valueOf(config.getChatServerPort())));
        
        // add to networksettings
        networkSettings.appendChild(serverAddress);
        networkSettings.appendChild(serverPort);
        networkSettings.appendChild(transferPort);
        networkSettings.appendChild(messagePort);
        networkSettings.appendChild(chatServerAddress);
        networkSettings.appendChild(chatServerPort);
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
    
    public String getChatString(String key){
        try{
            return ResourceBundle.getBundle("ikshare.client.gui.bundles.chatmessages",new Locale(config.getLanguage())).getString(key);
        }
        catch(MissingResourceException e){
            return key;
        }
    }
    
    public ClientConfiguration getConfiguration(){
    	if (config == null)
    		loadConfiguration();
        return config;
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
