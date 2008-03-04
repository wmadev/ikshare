/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;

/**
 *
 * @author awosy
 */
public class EventController {
    private static EventController instance;
    
    private EventController(){
        
    }
    public static EventController getInstance()
    {
        if(instance == null)
            instance = new EventController();
        return instance;
    }
}
