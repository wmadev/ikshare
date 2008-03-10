/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain.event;

import ikshare.domain.Transfer;
import ikshare.domain.event.listener.FileTransferListener;
import java.util.ArrayList;

/**
 *
 * @author awosy
 */
public class EventController {
    private static EventController instance;
    
    private ArrayList<FileTransferListener> fileTransferListeners;
    
    private EventController(){
        fileTransferListeners = new ArrayList<FileTransferListener>();
    }
    public static EventController getInstance()
    {
        if(instance == null)
            instance = new EventController();
        return instance;
    }
    public void addFileTransferListener(FileTransferListener l){
        fileTransferListeners.add(l);
    }

    public void triggerDownloadCanceledEvent(Transfer transfer) {
        System.out.println("EVENT: Trigger download canceled");
        for(FileTransferListener listener:fileTransferListeners)
	{
		listener.transferCanceled(transfer);
	}
    }
    
    public void triggerDownloadStartedEvent(Transfer transfer)
    {
        System.out.println("EVENT: Trigger download started");
        for(FileTransferListener listener:fileTransferListeners)
	{
		listener.transferStarted(transfer);
	}
    }
    public void triggerDownloadStateChangedEvent(Transfer transfer){
        System.out.println("EVENT: Trigger download state changed");
        for(FileTransferListener listener:fileTransferListeners)
	{
		listener.transferStateChanged(transfer);
	}        
    }
    public void triggerDownloadFinishedEvent(Transfer transfer){
        System.out.println("EVENT: Trigger download finished");
        for(FileTransferListener listener:fileTransferListeners)
	{
		listener.transferFinished(transfer);
	}        
    }
    
}
