package ikshare.domain;

import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.FileTransferListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Date;

import sun.print.resources.serviceui;

/**
 *
 * @author jonas
 */
public class PeerFileUploadThread implements Runnable, FileTransferListener {

    private Socket sendSocket;
    private byte[] buffer;
    private File sendFile;
    private Transfer transfer;
    
    


    public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	public PeerFileUploadThread(Socket receiveSocket) {
		EventController.getInstance().addFileTransferListener(this);
        sendSocket = receiveSocket;
        transfer = new Transfer();
		transfer.setFileName("/testmiddelgroot.rar");
		transfer.setState(TransferState.UPLOADING);
		transfer.setId(new Date().toString());
		transfer.setNumberOfBlocksFinished(0);

        buffer = new byte[512];
    }

    public void run() {
        try {
            // timeout zetten en streams aanmaken
            
        	
        	sendSocket.setSoTimeout(5000);
            BufferedOutputStream outStream = new BufferedOutputStream(sendSocket.getOutputStream());
            
            sendFile = new File(transfer.getFileName());
            transfer.setFileSize(sendFile.length());
            transfer.setNumberOfBlocks((int) (transfer.getFileSize()/512));
            EventController.getInstance().triggerDownloadStartedEvent(transfer);

            FileInputStream fileInput = new FileInputStream(sendFile.getAbsolutePath());

            int sentBytes = 0;
           // int aantalpakketjes=0;

            while ((sentBytes = fileInput.read(buffer)) > 0) {
            	transfer.setNumberOfBlocksFinished(transfer.getNumberOfBlocksFinished()+1);
            	transfer.setSpeed(transfer.getNumberOfBlocksFinished()*512);
            	EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
                outStream.write(buffer, 0, sentBytes);
                //System.out.println(buffer + " " + aantalpakketjes);
            }
            buffer=null;
            buffer=new byte[512];
            /*
            sentBytes =0;
            outStream.write(buffer,0,sentBytes);
            System.out.println("senden gedaan");
             */
            transfer.setState(TransferState.FINISHED);
            EventController.getInstance().triggerDownloadFinishedEvent(transfer);
            outStream.close();
            fileInput.close();
            sendSocket.close();
        } catch (Exception e) {
        	transfer.setState(TransferState.FAILED);
        	EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
            e.printStackTrace();
        }

    }
    
    public void stop() {
    	
    }

	public void transferCanceled(Transfer transfer) {
		// TODO Auto-generated method stub
		
	}

	public void transferFailed(Transfer transfer) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
}
