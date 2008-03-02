/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;

import java.util.ResourceBundle;

/**
 *
 * @author jonas
 */
public class ResourceBundleManager {
    private static ResourceBundleManager resourceBundleManager;
    private static ResourceBundle commandoBundle;    
    
    private ResourceBundleManager() {
        commandoBundle = ResourceBundle.getBundle("ikshare.protocol.command.Commando");
    }
    
    public static ResourceBundleManager getInstance() {
        if (resourceBundleManager == null) {
            resourceBundleManager = new ResourceBundleManager();
        }
        return resourceBundleManager;
    }

    public ResourceBundle getCommandoBundle() {
        return commandoBundle;
    }
    
    

}
