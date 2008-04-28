package ikshare.domain;

import ikshare.domain.event.EventController;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

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
		System.out.println("uploadthread");
		sendSocket = receiveSocket;

		buffer = new byte[2048];
	}

	public void run() {
		try {
			// timeout zetten en streams aanmaken
			transfer.setState(TransferState.UPLOADING);
			
			PeerFacade.getInstance().increaseActiveUpload();

			sendSocket.setSoTimeout(10000);
			outStream = new BufferedOutputStream(sendSocket.getOutputStream());

			sendFile = transfer.getFile();
			System.out.println(sendFile.getAbsolutePath());

			transfer.setFileSize(sendFile.length());
			
			EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
			EventController.getInstance().triggerActiveUploadsChanged();
			
			fileInput = new FileInputStream(sendFile.getAbsolutePath());
			fileInput.skip(transfer.getNumberOfBytesFinished());
			
			Date previousTime = new Date();
			Date now = null;
			int sentBytes = 0;
			long previousTotal=0;
			
			while (!sendSocket.isClosed() && outStream != null
					&& (sentBytes = fileInput.read(buffer)) > 0) {
				outStream.write(buffer, 0, sentBytes);
				outStream.flush();
				transfer.setNumberOfBytesFinished(transfer.getNumberOfBytesFinished() + sentBytes);
				now = new Date();

				if ((now.getTime() - previousTime.getTime()) >= 1000) {
				
					transfer.setSpeed((transfer.getNumberOfBytesFinished()-previousTotal) * 1000
							/ (Math.max(now.getTime() - previousTime.getTime(), 1)));
					transfer.setRemainingTime((transfer.getFileSize() - transfer.getNumberOfBytesFinished()) / transfer.getSpeed());
	
					

					previousTime = now;
					previousTotal = transfer.getNumberOfBytesFinished();
					EventController.getInstance().triggerDownloadStateChangedEvent(transfer);
				}
			}

			transfer.setState(TransferState.FINISHED);
			EventController.getInstance()
					.triggerDownloadFinishedEvent(transfer);
			
		} catch (Exception e) {
			if (transfer.getState() == TransferState.CANCELLEDUPLOAD)
				EventController.getInstance().triggerDownloadCanceledEvent(transfer);
			else if (transfer.getState() == TransferState.PAUSEDUPLOAD)
				EventController.getInstance().triggerDownloadPausedEvent(transfer);
			else {
				transfer.setState(TransferState.FAILED);
				e.printStackTrace();
				EventController.getInstance().triggerDownloadFailedEvent(transfer);
			}
		} finally {
			stop();
			PeerFacade.getInstance().decreaseActiveUpload();
			EventController.getInstance().triggerActiveUploadsChanged();
		}
	}

	public void stop() {
		try {
			if (fileInput != null)
				fileInput.close();
			if (sendSocket != null)
				sendSocket.close();
			if (outStream != null)
				outStream.close();
			fileInput = null;
			sendSocket = null;
			outStream = null;
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Uploadthread gestopt");
	}

}
