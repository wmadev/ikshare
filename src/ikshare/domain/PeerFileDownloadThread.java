package ikshare.domain;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jonas
 */
public class PeerFileDownloadThread implements Runnable {

    private BufferedInputStream inStream;
    private FileOutputStream fileOutput;
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

    public PeerFileDownloadThread(InetAddress address, Transfer transfer) {
        try {
            receiveSocket = new Socket(address, ClientConfigurationController.getInstance().getConfiguration().getFileTransferPort());
            receiveSocket.setSoTimeout(10000);
            buffer = new byte[2048];
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTransfer(transfer);
	}

	public void run() {
        try {
            outputFile = new File(transfer.getFile().getName());
            fileOutput = new FileOutputStream(outputFile);
            inStream = new BufferedInputStream( receiveSocket.getInputStream());
            
            int n;

            // Zolang er input komt van de socket moet er worden weggeschreven naar het bestand.
            // Om de seconde wordt een event getriggerd met de gemiddelde snelheid en de resterende downloadtijd
            //int tellerpakketjes=0;
            Date startUpload = new Date();
            Date now = null;
            while (!receiveSocket.isClosed() && inStream != null && (n = inStream.read(buffer)) > 0) {
                transfer.setNumberOfBlocksFinished(transfer.getNumberOfBlocksFinished()+1);

                now = new Date();
                
                transfer.setSpeed(transfer.getNumberOfBlocksFinished()*transfer.getBlockSize()*1000/(Math.max(now.getTime()-startUpload.getTime(), 1)));
                transfer.setRemainingTime((now.getTime()-startUpload.getTime())/(transfer.getNumberOfBlocksFinished())*(transfer.getNumberOfBlocks()-transfer.getNumberOfBlocksFinished())/1000);

                EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
                
                fileOutput.write(buffer, 0, n);
                //System.out.println("aantal bytes="+ n +" aantal pakketjes:" +tellerpakketjes);
            }
            transfer.setState(TransferState.FINISHED);
            EventController.getInstance().triggerDownloadFinishedEvent(transfer);
            fileOutput.flush();

        } catch (Exception e) {
        	transfer.setState(TransferState.FAILED);
        	EventController.getInstance().triggerDownloadFailedEvent(transfer);
        } finally {
            try {
				fileOutput.close();
	            inStream.close();
	            inStream = null;
	            receiveSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    }
    
    public void start() {
        service = Executors.newFixedThreadPool(1);
        service.execute(this);
        System.out.println("Downloadthread gestart.");
    }
    
    public void stop() {
    	try {
    		if (receiveSocket != null)
    			receiveSocket.close();
    		if (inStream != null)
    			inStream.close();
		} catch (IOException e) {
			transfer.setState(TransferState.FAILED);
			EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
			e.printStackTrace();
		} finally {
			service.shutdown();
			System.out.println("Downloadthread gestopt.");
		}
    }
}
