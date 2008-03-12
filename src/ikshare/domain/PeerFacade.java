package ikshare.domain;

import ikshare.domain.event.EventController;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PeerFacade {
	private static PeerFacade instance;
	private Peer peer;
	private PeerFileServer peerFileServer;
	private PeerFileDownloadThread peerFileDownloadThread;
	private Transfer uploadTransfer, downloadTransfer;
	
	
	

	public Transfer getDownloadTransfer() {
		return downloadTransfer;
	}

	public void setDownloadTransfer(Transfer downloadTransfer) {
		this.downloadTransfer = downloadTransfer;
	}

	public Transfer getUploadTransfer() {
		return uploadTransfer;
	}

	public void setUploadTransfer(Transfer uploadTransfer) {
		this.uploadTransfer = uploadTransfer;
	}

	protected PeerFacade() {
		
		//test
		
		
		peerFileServer = new PeerFileServer();
		peerFileServer.start();
	}
	
	public void startDownloadThread(Transfer transfer) {
		peer = new Peer();
		try {
			peerFileDownloadThread = new PeerFileDownloadThread(InetAddress.getLocalHost());
			peerFileDownloadThread.setTransfer(transfer);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		peerFileDownloadThread.start();
	}
	
	
	public PeerFileDownloadThread getPeerFileDownloadThread() {
		return peerFileDownloadThread;
	}



	public PeerFileServer getPeerFileServer() {
		return peerFileServer;
	}


	public static PeerFacade getInstance() {
		if (instance==null) {
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
	


}
