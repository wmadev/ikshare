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
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
        	PeerFacade.getInstance().increaseActiveDownload();
			EventController.getInstance().triggerActiveDownloadsChanged();
			
        	transfer.setState(TransferState.DOWNLOADING);

        	boolean resumingDownload = (transfer.getDownloadLocation()!="");
        	boolean superFolderExists = false;
        	
        	StringTokenizer pathTokenizer = new StringTokenizer(transfer.getFile().getFolder(), System.getProperty("file.separator"));
        	Vector<String> appartFolders = new Vector<String>();
        	while (pathTokenizer.hasMoreTokens()) {
        		String temp=pathTokenizer.nextToken();
        		appartFolders.add(temp);
        	}
        	int begin=0;
        	String fullPath=ClientConfigurationController.getInstance().getConfiguration().getSharedFolder().toString();
        	while (begin<appartFolders.size() && !superFolderExists) {
        		int i = begin;
        		fullPath=ClientConfigurationController.getInstance().getConfiguration().getSharedFolder().toString();
        		while (i<appartFolders.size()) {
        			fullPath += System.getProperty("file.separator") + appartFolders.get(i);
        			i++;
        		}
        		superFolderExists = new File(fullPath).isDirectory();

        		begin++;
        	}

        	

        	String fileNamePrefix, extension;
        	
        	if(!resumingDownload){
        		String fileName;
        		fileName = fullPath+System.getProperty("file.separator")+transfer.getFile().getName();
        		
        		
        		int lastIndexOfPoint = fileName.lastIndexOf(".");
	        	if (lastIndexOfPoint != -1) {
	        		fileNamePrefix = fileName.substring(0, lastIndexOfPoint);
	        		extension=fileName.substring(lastIndexOfPoint);
	        	} else {
	        		fileNamePrefix = fileName;
	        		extension = "";
	        	}
	
	        	outputFile = new File(fileNamePrefix+extension);
	            int fileNumber=1;
	            while (outputFile.exists()) {
	            	 outputFile = new File(fileNamePrefix + "[" + fileNumber + "]" + extension);
	            	 fileNumber++;
	            }
	            transfer.setDownloadLocation(outputFile.getAbsolutePath());
        	} else {
        		outputFile = new File(transfer.getDownloadLocation());
        	}
            
            fileOutput = new FileOutputStream(outputFile, resumingDownload);
            inStream = new BufferedInputStream( receiveSocket.getInputStream());
            
            int n;
            long previousTotal=0;

            // Zolang er input komt van de socket moet er worden weggeschreven naar het bestand.
            // Om de seconde wordt een event getriggerd met de gemiddelde snelheid en de resterende downloadtijd
            //int tellerpakketjes=0;
            Date previousTime = new Date();
            Date now = null;
            while (!receiveSocket.isClosed() && inStream != null && (n = inStream.read(buffer)) > 0) {
                
                fileOutput.write(buffer, 0, n);
                transfer.setNumberOfBytesFinished(transfer.getNumberOfBytesFinished()+n);
                
                now = new Date();
                
                
            	
				if ((now.getTime() - previousTime.getTime()) >= 1000) {
					transfer.setSpeed((transfer.getNumberOfBytesFinished()-previousTotal)*1000/(Math.max(now.getTime()-previousTime.getTime(), 1)));
					transfer.setRemainingTime((transfer.getFileSize() - transfer.getNumberOfBytesFinished()) / transfer.getSpeed());
					

					previousTime = now;
					previousTotal = transfer.getNumberOfBytesFinished();
					EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
				}
            }
            transfer.setState(TransferState.FINISHED);
            transfer.setSpeed(0);
            EventController.getInstance().triggerDownloadFinishedEvent(transfer);
            fileOutput.flush();

        } catch (Exception e) {
			if (transfer.getState() == TransferState.CANCELLEDDOWNLOAD)
				EventController.getInstance().triggerDownloadCanceledEvent(transfer);
			else if (transfer.getState() == TransferState.PAUSEDDOWNLOAD)
				EventController.getInstance().triggerDownloadPausedEvent(transfer);
			else {
				transfer.setState(TransferState.FAILED);
				EventController.getInstance().triggerDownloadFailedEvent(transfer);
			}
        } finally {
            stop();
            PeerFacade.getInstance().decreaseActiveDownload();
			EventController.getInstance().triggerActiveDownloadsChanged();
        }
    }
    
    public void start() {
        service = Executors.newFixedThreadPool(1);
        service.execute(this);
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
		}
    }
}
