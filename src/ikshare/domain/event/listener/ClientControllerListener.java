package ikshare.domain.event.listener;

import ikshare.domain.SearchResult;

public interface ClientControllerListener {

    public void connectionInterrupted();
    public void onLogOn();
    public void onLogOnFailed(String message);
    public void onResultFound(SearchResult found,String keyword);
    public void onNoResultFound(String keyword);
    
}
