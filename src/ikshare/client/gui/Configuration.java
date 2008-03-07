/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.client.gui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author awosy
 */
public class Configuration {
    private static Configuration instance;
    private ResourceBundle bundle;
    
    private Configuration(){
        bundle = ResourceBundle.getBundle("ikshare.client.gui.bundles.messages");
    }

    public static Configuration getInstance(){
        if(instance == null)
            instance = new Configuration();
        return instance;
    }
    public void changeLanguage(String locale){
        bundle = ResourceBundle.getBundle("ikshare.client.gui.bundles.messages_"+locale);
     
    }
    public String getString(String key){
        try{
            return bundle.getString(key);
        }
        catch(MissingResourceException e){
            return "-- KeyNotFound --";
        }
    }
}
