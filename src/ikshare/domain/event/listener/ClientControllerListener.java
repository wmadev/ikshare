/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain.event.listener;

/**
 *
 * @author awosy
 */
public interface ClientControllerListener {
    public void onLogOn();
    public void onLogOnFailed(String message);
}
