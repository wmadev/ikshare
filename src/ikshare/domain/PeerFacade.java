package ikshare.domain;

import ikshare.protocol.command.FileRequestCommando;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class PeerFacade {
	private static PeerFacade instance;

	private Peer peer, otherPeer;

	private PeerFileServer peerFileServer;

	private ArrayList<Transfer> uploadTransfers, downloadTransfers;

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
			otherPeer = new Peer("Degas", InetAddress.getByName("192.168.1.4"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		peerFileServer = new PeerFileServer();
		peerFileServer.start();

		downloadTransfers = new ArrayList<Transfer>();
		uploadTransfers = new ArrayList<Transfer>();

		peerMessageService = new PeerMessageService();
	}

	public void startDownloadThread(Transfer transfer) {
		PeerFileDownloadThread peerFileDownloadThread;
		try {
			peerFileDownloadThread = new PeerFileDownloadThread(
					InetAddress.getLocalHost(), transfer);

			peerFileDownloadThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startUploadThread(Transfer transfer) {
		PeerFileDownloadThread peerFileDownloadThread;
		try {
			peerFileDownloadThread = new PeerFileDownloadThread(
					InetAddress.getLocalHost(), transfer);

			peerFileDownloadThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addToDownloads(Transfer transfer) {

		downloadTransfers.add(transfer);
		
		FileRequestCommando frc = new FileRequestCommando();
		frc.setAccountName(otherPeer.getAccountName());
		frc.setPath(transfer.getFile().getParent());
		frc.setFileName(transfer.getFile().getName());
		try {
			peerMessageService.sendMessage(new Socket(otherPeer.getInternetAddress(), 6000), frc);
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

}
