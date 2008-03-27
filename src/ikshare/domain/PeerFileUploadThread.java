package ikshare.domain;

import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.FileTransferListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author jonas
 */
public class PeerFileUploadThread extends Thread implements Runnable,
		FileTransferListener {

	private Socket sendSocket;

	private byte[] buffer;

	private File sendFile;

	private Transfer transfer;

	private BufferedOutputStream outStream;

	private FileInputStream fileInput;

	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	public PeerFileUploadThread(Socket receiveSocket) {
		EventController.getInstance().addFileTransferListener(this);
		sendSocket = receiveSocket;

		buffer = new byte[2048];
	}

	public void run() {
		try {
			// timeout zetten en streams aanmaken

			sendSocket.setSoTimeout(5000);
			outStream = new BufferedOutputStream(sendSocket.getOutputStream());

			sendFile = transfer.getFile();
			transfer.setFileSize(sendFile.length());
			transfer.setNumberOfBlocks((int) (Math.ceil(transfer.getFileSize() / 2048)));
			EventController.getInstance().triggerDownloadStateChangedEvent(transfer);

			System.out.println(sendFile.getAbsolutePath());
			fileInput = new FileInputStream(sendFile.getAbsolutePath());

			Date startUpload = new Date();
			Date now=null;
			int sentBytes = 0;
			// int aantalpakketjes=0;
			while (!sendSocket.isClosed() && outStream != null
					&& (sentBytes = fileInput.read(buffer)) > 0) {
				
				outStream.write(buffer, 0, sentBytes);
				transfer.setNumberOfBlocksFinished(transfer
						.getNumberOfBlocksFinished() + 1);
				now = new Date();
				transfer.setSpeed(transfer.getNumberOfBlocksFinished()*2048/(now.getTime()-startUpload.getTime()+1));
                
				transfer.setRemainingTime((now.getTime()-startUpload.getTime())/(transfer.getNumberOfBlocksFinished())*(transfer.getNumberOfBlocks()-transfer.getNumberOfBlocksFinished())/1000);

				EventController.getInstance().triggerDownloadStateChangedEvent(
						transfer);

				//System.out.println(buffer + " " + aantalpakketjes);
			}
			buffer = null;
			buffer = new byte[512];
			/*
			 sentBytes =0;
			 outStream.write(buffer,0,sentBytes);
			 System.out.println("senden gedaan");
			 */
			transfer.setState(TransferState.FINISHED);
			EventController.getInstance()
					.triggerDownloadFinishedEvent(transfer);
		} catch (Exception e) {
			e.printStackTrace();
			transfer.setState(TransferState.FAILED);
			EventController.getInstance().triggerDownloadFailedEvent(transfer);
		} finally {
			try {
				System.out.println("upload stopped");
				fileInput.close();
				sendSocket.close();
				outStream.close();
				outStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public void transferCanceled(Transfer transfer) {
		// TODO Auto-generated method stub

	}

	public void transferFailed(Transfer transfer) {
		// TODO Auto-generated method stub
		System.out.println("transfer failed in PeerFileUploadThread");

	}

	public void transferFinished(Transfer transfer) {

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
