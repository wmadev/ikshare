package ikshare.domain;

import ikshare.client.configuration.ClientConfigurationController;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerFileServer extends Thread implements Runnable {
	private boolean running=false;
	private ServerSocket fileServerSocket;
	private ExecutorService service;
	
	public PeerFileServer() {
		running=true;
		try {
	        fileServerSocket = new ServerSocket(ClientConfigurationController.getInstance().getConfiguration().getFileTransferPort());
	        fileServerSocket.setReuseAddress(true);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	

	public void run() {
        try {
        	while (running) {
	            if (service!=null) {
	            	PeerFileUploadThread peerFileUploadThread = new PeerFileUploadThread(fileServerSocket.accept());
	            	peerFileUploadThread.setTransfer(PeerFacade.getInstance().getUploadTransferForFileName("testmiddelgroot.rar"));
	            	PeerFacade.getInstance().getUploadThreads().add(peerFileUploadThread);
	            	service.execute(peerFileUploadThread);
	            }
	            	
        	}
        } catch (Exception ex) {
           ex.printStackTrace();
        } finally {
            try {
                fileServerSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}
	
	@SuppressWarnings("unused")
	public void stopServer() throws InterruptedException {
		running=false;
		join();
	}
	
	public void startServer() {
		running=true;
        service = Executors.newCachedThreadPool();
	}
}
