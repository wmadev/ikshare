/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.data;

/**
 *
 * @author awosy
 */
public class DatabaseController {
    private static DatabaseController instance;
    
    private DatabaseController(){
        
    }
    
    public static DatabaseController getInstance(){
        if(instance == null)
            instance = new DatabaseController();
        return instance;
    }
}
