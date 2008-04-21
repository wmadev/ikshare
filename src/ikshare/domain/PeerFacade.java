package ikshare.domain;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.protocol.command.CancelTransferCommando;
import ikshare.protocol.command.FileRequestCommando;
import ikshare.protocol.command.PauseTransferCommando;
import ikshare.protocol.command.ResumeTransferCommando;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class PeerFacade {
	private static PeerFacade instance;

	private Peer peer, otherPeer;

	private PeerFileServer peerFileServer;

	private Vector<Transfer> uploadTransfers, downloadTransfers;
	private ArrayList<PeerFileDownloadThread> downloadThreads;
	private ArrayList<PeerFileUploadThread> uploadThreads;
	
	private int activeDownloads=0, activeUploads=0;
	
	private Queue<Transfer> transfersToStart;

	private PeerMessageService peerMessageService;

	public Peer getOtherPeer() {
		return otherPeer;
	}

	public void setOtherPeer(Peer otherPeer) {
		this.otherPeer = otherPeer;
	}

	public Vector<Transfer> getUploadTransfers() {
		return uploadTransfers;
	}

	public PeerMessageService getPeerMessageService() {
		return peerMessageService;
	}

	public void setPeerMessageService(PeerMessageService peerMessageService) {
		this.peerMessageService = peerMessageService;
	}

	protected PeerFacade() {
		
		//test
		try {
			peer = new Peer("Monet", InetAddress.getLocalHost());
			otherPeer = new Peer("Pizarro", InetAddress.getLocalHost());
			
			/*
			peer = new Peer("Pizarro", InetAddress.getLocalHost());
			otherPeer = new Peer("Monet", InetAddress.getByName("192.168.1.3"));
			*/
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		peerFileServer = new PeerFileServer();
		peerFileServer.startServer();

		downloadTransfers = new Vector<Transfer>();
		uploadTransfers = new Vector<Transfer>();
		
		downloadThreads = new ArrayList<PeerFileDownloadThread>();
		uploadThreads = new ArrayList<PeerFileUploadThread>();
		
		transfersToStart = new LinkedBlockingQueue<Transfer>();

		peerMessageService = new PeerMessageService();
	}



	public void addToDownloads(Transfer transfer) {		
		downloadTransfers.add(transfer);
		
		FileRequestCommando frc = new FileRequestCommando();

		frc.setAccountName(peer.getAccountName());
		frc.setPath(transfer.getFile().getFolder());
		frc.setFileName(transfer.getFile().getFileName());
		frc.setTransferId(transfer.getId());
		frc.setSentBytes(0);
		try {
			peerMessageService.setSendSocket(new Socket(otherPeer.getInternetAddress(), ClientConfigurationController.getInstance().getConfiguration().getMessagePort()));
			peerMessageService.sendMessage(frc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addToUploads(Transfer transfer) {
		uploadTransfers.add(transfer);
		transfersToStart.offer(transfer);
	}

	public Vector<Transfer> getDownloadTransfers() {
		return downloadTransfers;
	}

	public PeerFileServer getPeerFileServer() {
		return peerFileServer;
	}

	public static PeerFacade getInstance() {
		if (instance == null) {
			instance = new PeerFacade();
		}
		return instance;
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
	public Transfer getDownloadTransferForId(String transferId) {
		Transfer ret=null;
		boolean found = false;
		ListIterator<Transfer> iterator = downloadTransfers.listIterator();
		while(!found && iterator.hasNext()) {
			Transfer current = iterator.next();
			if (current.getId().equals(transferId)) {
				ret = current;
				found = true;
			}
		}
		return ret;
	}
	public Transfer getUploadTransferForId(String transferId) {
		Transfer ret=null;
		boolean found = false;
		ListIterator<Transfer> iterator = uploadTransfers.listIterator();
		while(!found && iterator.hasNext()) {
			Transfer current = iterator.next();
			if (current.getId().equals(transferId)) {
				ret = current;
				found = true;
			}
		}
		return ret;
	}
	public PeerFileDownloadThread getPeerFileDownloadThreadForTransfer(Transfer t) {
		return getPeerFileDownloadThreadForTransferId(t.getId());
	}
	public PeerFileUploadThread getPeerFileUploadThreadForTransfer(Transfer t) {
		return getPeerFileUploadThreadForTransferId(t.getId());
	}
	public PeerFileDownloadThread getPeerFileDownloadThreadForTransferId(String transferId) {
		PeerFileDownloadThread ret=null;
		boolean found = false;
		ListIterator<PeerFileDownloadThread> iterator = downloadThreads.listIterator();
		while(!found && iterator.hasNext()) {
			PeerFileDownloadThread current = iterator.next();
			if (current.getTransfer().getId().equals(transferId)) {
				ret = current;
				found = true;
			}
		}
		return ret;
	}
	public PeerFileUploadThread getPeerFileUploadThreadForTransferId(String transferId) {
		PeerFileUploadThread ret=null;
		boolean found = false;
		ListIterator<PeerFileUploadThread> iterator = uploadThreads.listIterator();
		while(!found && iterator.hasNext()) {
			PeerFileUploadThread current = iterator.next();
			if (current.getTransfer().getId().equals(transferId)) {
				ret = current;
				found = true;
			}
		}
		return ret;
	}

	public ArrayList<PeerFileDownloadThread> getDownloadThreads() {
		return downloadThreads;
	}

	public ArrayList<PeerFileUploadThread> getUploadThreads() {
		return uploadThreads;
	}

	public void startDownloadThread(Transfer transfer) {
		PeerFileDownloadThread peerFileDownloadThread;
		peerFileDownloadThread = new PeerFileDownloadThread(otherPeer.getInternetAddress(), transfer);
		downloadThreads.add(peerFileDownloadThread);
		peerFileDownloadThread.start();
	}
	
	public void startResumeThread(Transfer transfer) {
		PeerFileDownloadThread peerFileDownloadThread;
		peerFileDownloadThread = new PeerFileDownloadThread(otherPeer.getInternetAddress(), transfer);
		downloadThreads.add(peerFileDownloadThread);
		peerFileDownloadThread.start();
	}
	
	public void cancelDownloadThread(Transfer selected) {
		getPeerFileDownloadThreadForTransfer(selected).stop();
		selected.setState(TransferState.CANCELLEDDOWNLOAD);
		EventController.getInstance().triggerDownloadCanceledEvent(selected);
		
		CancelTransferCommando ctc = new CancelTransferCommando();
		ctc.setAccountName(getPeer().getAccountName());
		ctc.setTransferId(selected.getId());
		peerMessageService.sendMessage(ctc);
	}
	
	public void pauseDownloadThread(Transfer selected) {
		getPeerFileDownloadThreadForTransfer(selected).stop();
		selected.setState(TransferState.PAUSEDDOWNLOAD);
		EventController.getInstance().triggerDownloadPausedEvent(selected);
		
		PauseTransferCommando ptc = new PauseTransferCommando();
		ptc.setAccountName(peer.getAccountName());
		ptc.setSentBlocks(selected.getNumberOfBytesFinished());
		ptc.setTransferId(selected.getId());
		peerMessageService.sendMessage(ptc);
	}

	public void resumeDownloadThread(Transfer selected) {
		selected.setState(TransferState.RESUMEDDOWNLOAD);
		EventController.getInstance().triggerDownloadResumedEvent(selected);
		
		FileRequestCommando frc = new FileRequestCommando();
	
		File receivedFile = new File(selected.getDownloadLocation());
		selected.setNumberOfBytesFinished(receivedFile.length());
		
		
		frc.setAccountName(peer.getAccountName());
		frc.setPath(selected.getFile().getFolder());
		frc.setFileName(selected.getFile().getFileName());
		frc.setTransferId(selected.getId());
		frc.setSentBytes(selected.getNumberOfBytesFinished());
		
		peerMessageService.sendMessage(frc);
	}

	public int getActiveDownloads() {
		return activeDownloads;
	}

	public void setActiveDownloads(int activeDownloads) {
		this.activeDownloads = activeDownloads;
	}

	public int getActiveUploads() {
		return activeUploads;
	}

	public void setActiveUploads(int activeUploads) {
		this.activeUploads = activeUploads;
	}
	
	public void increaseActiveUpload() {
		activeUploads++;
	}
	
	public void increaseActiveDownload() {
		activeDownloads++;
	}
	
	public void decreaseActiveUpload() {
		activeUploads--;
	}
	
	public void decreaseActiveDownload() {
		activeDownloads--;
	}

	public Queue<Transfer> getTransfersToStart() {
		return transfersToStart;
	}
	
	
	
}
