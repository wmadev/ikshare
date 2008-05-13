package ikshare.domain;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.protocol.command.CancelTransferCommando;
import ikshare.protocol.command.FileRequestCommando;
import ikshare.protocol.command.PauseTransferCommando;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class PeerFacade {
	private static PeerFacade instance;

	private Peer peer;

	private PeerFileServer peerFileServer;

	private Vector<Transfer> uploadTransfers, downloadTransfers;
	private ArrayList<PeerFileDownloadThread> downloadThreads;
	private ArrayList<PeerFileUploadThread> uploadThreads;
	
	private HashMap<String, PeerMessageThread> messageThreads;
	
	private int activeDownloads=0, activeUploads=0;
	
	private Queue<Transfer> uploadQueue;

	private PeerMessageService peerMessageService;
	
	
	private ExecutorService executorService;
	
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
		/*try {
			peer = new Peer("jonas", InetAddress.getByName(ClientConfigurationController.getInstance().getConfiguration().getMyAddress()));
			
			//System.out.println(InetAddress.);
			/*
			peer = new Peer("Pizarro", InetAddress.getLocalHost());
			otherPeer = new Peer("Monet", InetAddress.getByName("192.168.1.3"));
			*//*
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		peerFileServer = new PeerFileServer();
		peerFileServer.startServer();

		downloadTransfers = new Vector<Transfer>();
		uploadTransfers = new Vector<Transfer>();
		
		downloadThreads = new ArrayList<PeerFileDownloadThread>();
		uploadThreads = new ArrayList<PeerFileUploadThread>();
		
		uploadQueue = new LinkedBlockingQueue<Transfer>();

		messageThreads = new HashMap<String, PeerMessageThread>();
		
		executorService = Executors.newCachedThreadPool();
		
		peerMessageService = new PeerMessageService();
		executorService.execute(peerMessageService);
		
		folderMap = new HashMap<Integer, SearchResult>();
	}



	public void addToDownloads(Transfer transfer) {		
		downloadTransfers.add(transfer);
		
		FileRequestCommando frc = new FileRequestCommando();

		frc.setAccountName(peer.getAccountName());
		frc.setPath(transfer.getFile().getFolder());
		frc.setFileName(transfer.getFile().getFileName());
		frc.setTransferId(transfer.getId());
		frc.setSentBytes(0);
		PeerMessageThread peerMessageThread = new PeerMessageThread(transfer);
		executorService.execute(peerMessageThread);
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		
		peerMessageThread.sendMessage(frc);
		
		messageThreads.put(transfer.getId(), peerMessageThread);

	}
	
	public void addToUploads(Transfer transfer) {
		uploadTransfers.add(transfer);
		uploadQueue.offer(transfer);
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
		peerFileDownloadThread = new PeerFileDownloadThread(transfer.getPeer().getInternetAddress(), transfer);
		downloadThreads.add(peerFileDownloadThread);
		peerFileDownloadThread.start();
	}
	
	public void startResumeThread(Transfer transfer) {
		PeerFileDownloadThread peerFileDownloadThread;
		peerFileDownloadThread = new PeerFileDownloadThread(transfer.getPeer().getInternetAddress(), transfer);
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

		messageThreads.get(selected.getId()).sendMessage(ctc);

	}
	
	public void pauseDownloadThread(Transfer selected) {
		getPeerFileDownloadThreadForTransfer(selected).stop();
		selected.setState(TransferState.PAUSEDDOWNLOAD);
		EventController.getInstance().triggerDownloadPausedEvent(selected);
		
		PauseTransferCommando ptc = new PauseTransferCommando();
		ptc.setAccountName(peer.getAccountName());
		ptc.setSentBlocks(selected.getNumberOfBytesFinished());
		ptc.setTransferId(selected.getId());

		
		messageThreads.get(selected.getId()).sendMessage(ptc);

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
		
		messageThreads.get(selected.getId()).sendMessage(frc);
	}

	public void clearTransfers() {
		ListIterator<Transfer> iterator = downloadTransfers.listIterator();
		Transfer transfer = null;
		while(iterator.hasNext()) {
			transfer = iterator.next();
			if (transfer.getState()==TransferState.FINISHED || 
					transfer.getState()==TransferState.FAILED || 
					transfer.getState()==TransferState.CANCELLEDDOWNLOAD) {
				iterator.remove();
			}
		}
		iterator = uploadTransfers.listIterator();
		while(iterator.hasNext()) {
			transfer = iterator.next();
			if (transfer.getState()==TransferState.FINISHED || 
					transfer.getState()==TransferState.FAILED || 
					transfer.getState()==TransferState.CANCELLEDUPLOAD) {
				iterator.remove();
			}
		}
		EventController.getInstance().triggerClearTransfers();
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

	public Queue<Transfer> getUploadQueue() {
		return uploadQueue;
	}

	public void makeFolder(SearchResult start) {
		
		folderMap.put(start.getFolderId(), start);
		
		String folderName = "";//ClientConfigurationController.getInstance().getConfiguration().getSharedFolder().toString();
		
		SearchResult temp = start;
		while (temp!=null) {
			folderName = temp.getName() + System.getProperty("file.separator") + folderName;
			temp = folderMap.get(temp.getParentId());
		}
		File folder = new File(ClientConfigurationController.getInstance().getConfiguration().getSharedFolder()+folderName);

		System.out.println("[PeerFacade-makeFolder]: foldername:" + folderName );
		System.out.println("[PeerFacade-makeFolder]: folder:" + folder );

		
		if (!folder.exists());
			folder.mkdir();
	}

	public HashMap<Integer, SearchResult> getFolderMap() {
		return folderMap;
	}

	private HashMap<Integer, SearchResult> folderMap;
	
	
}
