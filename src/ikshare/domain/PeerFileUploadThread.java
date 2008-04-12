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
public class PeerFileUploadThread implements Runnable {

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
		sendSocket = receiveSocket;

		buffer = new byte[2048];
	}

	public void run() {
		try {
			// timeout zetten en streams aanmaken

			sendSocket.setSoTimeout(10000);
			outStream = new BufferedOutputStream(sendSocket.getOutputStream());

			sendFile = transfer.getFile();
			transfer.setFileSize(sendFile.length());
			transfer.setNumberOfBlocks((int) (Math.ceil(transfer.getFileSize() / 2048)));
			EventController.getInstance().triggerDownloadStateChangedEvent(transfer);

			fileInput = new FileInputStream(sendFile.getAbsolutePath());

			Date startUpload = new Date();
			Date now=null;
			int sentBytes = 0;
			while (!sendSocket.isClosed() && outStream != null && (sentBytes = fileInput.read(buffer)) > 0) {
				outStream.write(buffer, 0, sentBytes);
				outStream.flush();
				transfer.setNumberOfBlocksFinished(transfer.getNumberOfBlocksFinished() + 1);
				now = new Date();

                transfer.setSpeed(transfer.getNumberOfBlocksFinished()*transfer.getBlockSize()*1000/(Math.max(now.getTime()-startUpload.getTime(), 1)));
                transfer.setRemainingTime((now.getTime()-startUpload.getTime())/(transfer.getNumberOfBlocksFinished())*(transfer.getNumberOfBlocks()-transfer.getNumberOfBlocksFinished())/1000);

				EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
			}

			transfer.setState(TransferState.FINISHED);
			EventController.getInstance().triggerDownloadFinishedEvent(transfer);
		} catch (Exception e) {
			if (transfer.getState() != TransferState.CANCELEDUPLOAD) {
				transfer.setState(TransferState.FAILED);
				e.printStackTrace();
				EventController.getInstance().triggerDownloadFailedEvent(transfer);
			} else {
				EventController.getInstance().triggerDownloadCanceledEvent(transfer);
			}
		} finally {
			stop();
		}
	}
	
	public void stop() {
		try {
			fileInput.close();
			sendSocket.close();
			outStream.close();
			outStream = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Uploadthread gestopt");
	}
	
}
