/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain.event.listener;

import ikshare.domain.SearchResult;

/**
 *
 * @author awosy
 */
public interface ClientControllerListener {

    public void connectionInterrupted();
    public void onLogOn();
    public void onLogOnFailed(String message);
    public void onResultFound(SearchResult found,String keyword);
    public void onNoResultFound(String keyword);
    
}
