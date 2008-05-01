/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.exceptions;

/**
 *
 * @author awosy
 */
public class ConfigurationException extends Exception{
    
    public ConfigurationException(String message){
        super(message);
    }
    public ConfigurationException(String message, Exception e){
        super(message,e);
    }

}
