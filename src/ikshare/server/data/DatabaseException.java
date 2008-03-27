/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.server.data;

/**
 *
 * @author awosy
 */
public class DatabaseException extends Exception {
    public DatabaseException(String message){
        super(message);
    }
    
    public String toString(){
        return this.getMessage();
    }
}
