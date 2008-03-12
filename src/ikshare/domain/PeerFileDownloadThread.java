package ikshare.domain;

import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.FileTransferListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jonas
 */
public class PeerFileDownloadThread implements Runnable, FileTransferListener {

    private BufferedInputStream inStream;
    private File outputFile;
    private byte[] buffer;
    private Socket receiveSocket;
    private ExecutorService service;
    private Transfer transfer;
    
    

    public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	public PeerFileDownloadThread(InetAddress address) {
		EventController.getInstance().addFileTransferListener(this);
        try {
            receiveSocket = new Socket(address, 6002);
            receiveSocket.setSoTimeout(5000);
            buffer = new byte[512];
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void run() {
        try {
            outputFile = new File("/kopie.ext");
            FileOutputStream fileOutput = new FileOutputStream(outputFile);
            inStream = new BufferedInputStream( receiveSocket.getInputStream());
            
            int n;

            // Zolang er input komt van de socket moet er worden weggeschreven naar het bestand.
            // Om de seconde wordt een event getriggerd met de gemiddelde snelheid en de resterende downloadtijd
            //int tellerpakketjes=0;
            while ((n = inStream.read(buffer)) > 0) {
                transfer.setNumberOfBlocksFinished(transfer.getNumberOfBlocksFinished()+1);
                transfer.setSpeed(transfer.getNumberOfBlocksFinished()*512);
                EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
                
                fileOutput.write(buffer, 0, n);
                //System.out.println("aantal bytes="+ n +" aantal pakketjes:" +tellerpakketjes);
            }
            transfer.setState(TransferState.FINISHED);
            EventController.getInstance().triggerDownloadFinishedEvent(transfer);
            fileOutput.flush();
            fileOutput.close();
            inStream.close();
            receiveSocket.close();
        } catch (Exception e) {
        	transfer.setState(TransferState.FAILED);
        	
        	EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
            if (!receiveSocket.isClosed()) {
                try {
                    receiveSocket.close();
                } catch (IOException ex) {
                    e.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }
    
    public void start() {
        service = Executors.newFixedThreadPool(1);
        service.execute(this);
        System.out.println("dus..");
    }
    
    public void stop() {
    	service.shutdown();
    }

	public void transferCanceled(Transfer transfer) {
		stop();
		
	}

	public void transferFailed(Transfer transfer) {
		stop();
		
	}

	public void transferFinished(Transfer transfer) {
		stop();
		
	}

	public void transferStarted(Transfer transfer) {
		// TODO Auto-generated method stub
		
	}

	public void transferStateChanged(Transfer transfer) {
		// TODO Auto-generated method stub
		
	}

	public void transferStopped(Transfer transfer) {
		stop();
		
	}
}
