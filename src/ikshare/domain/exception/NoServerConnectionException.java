/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain.exception;

/**
 *
 * @author awosy
 */
public class NoServerConnectionException extends Exception {
    public NoServerConnectionException(String message){
        super(message);
    }
}
