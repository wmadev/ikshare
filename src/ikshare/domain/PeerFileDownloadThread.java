package ikshare.domain;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    	System.out.println("PeerFileDownloadThread aanmaken");
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
        	
        	boolean resumingDownload = (transfer.getDownloadLocation()!="");
        	
        	if(!resumingDownload)
        	{
	        	String fileName = ClientConfigurationController.getInstance().getConfiguration().getSharedFolder()+System.getProperty("file.separator")+transfer.getFile().getName();
	        	int lastIndexOfPoint = fileName.lastIndexOf(".");
	        	System.out.println(lastIndexOfPoint);
	        	String fileNamePrefix = fileName.substring(0, lastIndexOfPoint);
	        	String extension=fileName.substring(lastIndexOfPoint);
	
	        	outputFile = new File(fileNamePrefix+extension);
	            int fileNumber=1;
	            while (outputFile.exists()) {
	            	 outputFile = new File(fileNamePrefix + "[" + fileNumber + "]" + extension);
	            	 fileNumber++;
	            }
        	}
        	else
        	{
        		outputFile = new File(transfer.getDownloadLocation());
        	}
            
            fileOutput = new FileOutputStream(outputFile, resumingDownload);
            inStream = new BufferedInputStream( receiveSocket.getInputStream());
            
            int n;

            // Zolang er input komt van de socket moet er worden weggeschreven naar het bestand.
            // Om de seconde wordt een event getriggerd met de gemiddelde snelheid en de resterende downloadtijd
            //int tellerpakketjes=0;
            Date startDownload = new Date();
            Date now = null;
            while (!receiveSocket.isClosed() && inStream != null && (n = inStream.read(buffer)) > 0) {
                transfer.setNumberOfBytesFinished(transfer.getNumberOfBytesFinished()+n);

                now = new Date();
                
                transfer.setSpeed(transfer.getNumberOfBytesFinished()*1000/(Math.max(now.getTime()-startDownload.getTime(), 1)));
                transfer.setRemainingTime((now.getTime()-startDownload.getTime())/(transfer.getNumberOfBytesFinished())*(transfer.getFileSize()-transfer.getNumberOfBytesFinished())/1000);

                EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
                
                fileOutput.write(buffer, 0, n);
                //System.out.println("aantal bytes="+ n +" aantal pakketjes:" +tellerpakketjes);
            }
            transfer.setState(TransferState.FINISHED);
            EventController.getInstance().triggerDownloadFinishedEvent(transfer);
            fileOutput.flush();

        } catch (Exception e) {
			if (transfer.getState() == TransferState.CANCELLEDDOWNLOAD)
				EventController.getInstance().triggerDownloadCanceledEvent(transfer);
			else if (transfer.getState() == TransferState.PAUSEDDOWNLOAD)
				EventController.getInstance().triggerDownloadPausedEvent(transfer);
			else {
				transfer.setState(TransferState.FAILED);
				e.printStackTrace();
				EventController.getInstance().triggerDownloadFailedEvent(transfer);
			}
        } finally {
            stop();
        }
    }
    
    public void start() {
        service = Executors.newFixedThreadPool(1);
        service.execute(this);
        System.out.println("Downloadthread gestart.");
    }
    
    public void stop() {
    	try {
    		if (fileOutput != null)
    			fileOutput.close();
    		if (receiveSocket != null)
    			receiveSocket.close();
    		if (inStream != null)
    			inStream.close();
    		fileOutput = null;
    		receiveSocket = null;
    		inStream = null;
    		System.gc();
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
