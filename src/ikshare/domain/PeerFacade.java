package ikshare.domain;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.protocol.command.CancelTransferCommando;
import ikshare.protocol.command.FileRequestCommando;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;

public class PeerFacade {
	private static PeerFacade instance;

	private Peer peer, otherPeer;

	private PeerFileServer peerFileServer;

	private ArrayList<Transfer> uploadTransfers, downloadTransfers;
	private ArrayList<PeerFileDownloadThread> downloadThreads;
	private ArrayList<PeerFileUploadThread> uploadThreads;

	private PeerMessageService peerMessageService;

	public Peer getOtherPeer() {
		return otherPeer;
	}

	public void setOtherPeer(Peer otherPeer) {
		this.otherPeer = otherPeer;
	}

	public ArrayList<Transfer> getUploadTransfers() {
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
			otherPeer = new Peer("Pizarro", InetAddress.getByName("192.168.1.2"));
			
			/*
			peer = new Peer("Pizarro", InetAddress.getLocalHost());
			otherPeer = new Peer("Monet", InetAddress.getByName("192.168.1.3"));
			*/
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		peerFileServer = new PeerFileServer();
		peerFileServer.start();

		downloadTransfers = new ArrayList<Transfer>();
		uploadTransfers = new ArrayList<Transfer>();
		
		downloadThreads = new ArrayList<PeerFileDownloadThread>();
		uploadThreads = new ArrayList<PeerFileUploadThread>();

		peerMessageService = new PeerMessageService();
	}



	public void addToDownloads(Transfer transfer) {

		downloadTransfers.add(transfer);
		
		FileRequestCommando frc = new FileRequestCommando();
		frc.setAccountName(peer.getAccountName());
		frc.setPath("C://");
		frc.setFileName("testmiddelgroot.rar");
		frc.setTransferId(transfer.getId());
		try {
			peerMessageService.setSendSocket(new Socket(otherPeer.getInternetAddress(), ClientConfigurationController.getInstance().getConfiguration().getIkshareServerPort()));
			peerMessageService.sendMessage(frc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addToUploads(Transfer transfer) {
		uploadTransfers.add(transfer);
	}

	public ArrayList<Transfer> getDownloadTransfers() {
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

	@SuppressWarnings("unused")
	private void loadPeer() {
		//TODO: load peer from xml file;
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}
	
	public Transfer getDownloadTransferForFileName(String fileName) {
		Transfer ret=null;
		for (Transfer t: downloadTransfers) {
			if (t.getFile().getName().equals(fileName)) {
				ret = t;
			}
		}
		return ret;
	}
	public Transfer getUploadTransferForFileName(String fileName) {
		Transfer ret=null;
		for (Transfer t: uploadTransfers) {
			if (t.getFile()!=null && t.getFile().getName().equals(fileName)) {
				ret = t;
			}
		}
		return ret;
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
	
	public void cancelDownloadThread(Transfer selected) {
		// TODO Auto-generated method stub
		System.out.println(selected);
		getPeerFileDownloadThreadForTransfer(selected).stop();
		selected.setState(TransferState.CANCELEDDOWNLOAD);
		EventController.getInstance().triggerDownloadCanceledEvent(selected);
		
		CancelTransferCommando ctc = new CancelTransferCommando();
		ctc.setTransferId(selected.getId());
		peerMessageService.sendMessage(ctc);
	}
	
	public void pauseDownloadThread(Transfer selected) {
		// TODO Auto-generated method stub
		
	}

	public void resumeDownloadThread(Transfer selected) {
		// TODO Auto-generated method stub
		
	}

}
