package ikshare.domain;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.TransferQueueListener;
import ikshare.protocol.command.CancelTransferCommando;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CommandoParser;
import ikshare.protocol.command.FileConfirmCommando;
import ikshare.protocol.command.FileNotFoundCommando;
import ikshare.protocol.command.FileRequestCommando;
import ikshare.protocol.command.FoundItAllCommando;
import ikshare.protocol.command.GetConnCommando;
import ikshare.protocol.command.GiveConnCommando;
import ikshare.protocol.command.GivePeerCommando;
import ikshare.protocol.command.GoForItCommando;
import ikshare.protocol.command.MyTurnCommando;
import ikshare.protocol.command.PassTurnCommando;
import ikshare.protocol.command.PauseTransferCommando;
import ikshare.protocol.command.ResumeTransferCommando;
import ikshare.protocol.command.YourTurnCommando;
import ikshare.protocol.exception.CommandNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerMessageService extends Thread implements Runnable, TransferQueueListener{
    private ServerSocket messageServer;
    private PrintWriter printWriter;
    private boolean running;
	private int port;
	public PeerMessageService() {
    	running = true;
    	// Moet poort zijn van andere peer veronderstel ik? ipv van de ikshareserverpoort?
        // Best voor testen geen poorten gebruiken waar anderen verwachten dat iets anders werkt :p
        port = ClientConfigurationController.getInstance().getConfiguration().getMessagePort();//ClientConfigurationController.getInstance().getConfiguration().getIkshareServerPort();
    	try {
			messageServer = new ServerSocket(port);
			messageServer.setReceiveBufferSize(100);
			messageServer.setReuseAddress(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	ExecutorService es = Executors.newSingleThreadExecutor();
    	es.execute(this);
    }

	public void run() {
		while (running) {
	        Socket link = null;
	        BufferedReader in = null;
	        
	        try
	        {
	        	// Step 1. Establish server connection
	            link = messageServer.accept();
	            link.setSoTimeout(10000);
	            //System.out.println("verbonden");
	            /*
	            sendSocket = new Socket(link.getInetAddress(), ClientConfigurationController.getInstance().getConfiguration().getMessagePort());
	            sendSocket.setSoTimeout(10000);
	             */
	            
	             
	            // Step 2. Set up input stream
	            in = new BufferedReader(new
	                InputStreamReader(link.getInputStream()));
	            
	            // Step 3. Receive data
	            String input = in.readLine();
	            
	            while (input != null)
	            {
	            		//System.out.println(input);
		                Commando c = null;
		                try {
		                	c = CommandoParser.getInstance().parse(input);
		                } catch (CommandNotFoundException cnfe) {
		                	cnfe.printStackTrace();
		                }
		                handleCommando(link, c);
		                input = in.readLine();
	            }
	            //link.close();
	            //replySocket.close();
	        } catch (Exception e) {
	        	//TODO
	        }
		}
		
	}

	private void handleCommando(Socket sendSocket, Commando c) {
		System.out.println("Ontvangen Commando");
		System.out.println("------------------");
		System.out.println(c);
		System.out.println("");
		
		if (c instanceof FoundItAllCommando) {
			handleFoundItAllCommando(sendSocket, (FoundItAllCommando) c);
		}
		else if (c instanceof GivePeerCommando) {
			handleGivePeerCommando(sendSocket, (GivePeerCommando) c);
		}
		else if (c instanceof FileRequestCommando) {
			handleFileRequestCommando(sendSocket, (FileRequestCommando) c);
		}
		else if (c instanceof FileConfirmCommando) {
			handleFileConfirmCommando(sendSocket, (FileConfirmCommando) c);
		}
		else if (c instanceof FileNotFoundCommando) {
			handleFileNotFoundCommando(sendSocket, (FileNotFoundCommando) c);
		}
		else if (c instanceof YourTurnCommando) {
			handleYourTurnCommando(sendSocket, (YourTurnCommando) c);
		}
		else if (c instanceof MyTurnCommando) {
			handleMyTurnCommando(sendSocket, (MyTurnCommando) c);
		}
		else if (c instanceof PassTurnCommando) {
			handlePassTurnCommando(sendSocket, (PassTurnCommando) c);
		}
		else if (c instanceof GoForItCommando) {
			handleGoForItCommando(sendSocket, (GoForItCommando) c);
		}
		else if (c instanceof GetConnCommando) {
			handleGetConnCommando(sendSocket, (GetConnCommando) c);
		}
		else if (c instanceof GiveConnCommando) {
			handleGiveConnCommando(sendSocket, (GiveConnCommando) c);
		}
		else if (c instanceof CancelTransferCommando) {
			handleCancelTransferCommando(sendSocket, (CancelTransferCommando) c);
		}
		else if (c instanceof PauseTransferCommando) {
			handlePauseTransferCommando(sendSocket, (PauseTransferCommando) c);
		}
		else if (c instanceof ResumeTransferCommando) {
			handleResumeTransferCommando(sendSocket, (ResumeTransferCommando) c);
		}
		else if(c instanceof Commando) {
			handleOtherCommandos(sendSocket, c);
		}

	}



	private void handleGoForItCommando(Socket sendSocket, GoForItCommando gfic) {
		Transfer t = PeerFacade.getInstance().getDownloadTransferForId(gfic.getTransferId());
		PeerFacade.getInstance().startDownloadThread(PeerFacade.getInstance().getDownloadTransferForId(gfic.getTransferId()));	

	}

	private void handleOtherCommandos(Socket sendSocket, Commando c) {
		try {
			sendSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleCancelTransferCommando(Socket sendSocket, CancelTransferCommando ctc) {
		Transfer canceledTransfer = PeerFacade.getInstance().getUploadTransferForId(ctc.getTransferId());
		canceledTransfer.setState(TransferState.CANCELLEDUPLOAD);
		EventController.getInstance().triggerDownloadCanceledEvent(canceledTransfer);
		
		PeerFacade.getInstance().getPeerFileUploadThreadForTransfer(canceledTransfer).stop();
	}

	private void handlePauseTransferCommando(Socket sendSocket, PauseTransferCommando ptc) {
		Transfer pausedTransfer = PeerFacade.getInstance().getUploadTransferForId(ptc.getTransferId());
		pausedTransfer.setState(TransferState.PAUSEDUPLOAD);
		EventController.getInstance().triggerDownloadPausedEvent(pausedTransfer);
		
		PeerFacade.getInstance().getPeerFileUploadThreadForTransfer(pausedTransfer).stop();
	}
	
	private void handleResumeTransferCommando(Socket sendSocket, ResumeTransferCommando rtc) {
		Transfer resumedTransfer = PeerFacade.getInstance().getUploadTransferForId(rtc.getTransferId());
		resumedTransfer.setState(TransferState.RESUMEDUPLOAD);
		EventController.getInstance().triggerDownloadResumedEvent(resumedTransfer);
		
		PeerFacade.getInstance().startResumeThread(PeerFacade.getInstance().getDownloadTransferForId(rtc.getTransferId()));	
	}

	private void handleGiveConnCommando(Socket sendSocket, GiveConnCommando givecc) {
		// TODO handleGiveConnCommando
		
	}

	private void handleGetConnCommando(Socket sendSocket, GetConnCommando getcc) {
		// TODO handleGetConnCommando
	}

	private void handlePassTurnCommando(Socket sendSocket, PassTurnCommando ptc) {
		// TODO handlePassTurnCommando
		
	}

	private void handleMyTurnCommando(Socket sendSocket, MyTurnCommando mtc) {
		GoForItCommando gfic = new GoForItCommando();
		gfic.setAccountName(PeerFacade.getInstance().getPeer().getAccountName());
		gfic.setTransferId(mtc.getTransferId());
		sendMessage(sendSocket, gfic);		
	}

	private void handleGivePeerCommando(Socket sendSocket, GivePeerCommando gpc) {
		// TODO handleGivePeerCommando
		
	}

	private void handleFoundItAllCommando(Socket sendSocket, FoundItAllCommando fiac) {
		// TODO handleFoundItallCommando
		
	}

	private void handleFileRequestCommando(Socket sendSocket, FileRequestCommando frc) {
		//File f = new File(frc.getPath().replace('\\', '/')+frc.getFileName());
		IKShareFile f = new IKShareFile(frc.getPath(),frc.getFileName());
		
		if( f.exists() ) {
	    	FileConfirmCommando fcc = new FileConfirmCommando();
	    	fcc.setAccountName(PeerFacade.getInstance().getPeer().getAccountName());
	    	fcc.setFileName(frc.getFileName());
	    	fcc.setPath(frc.getPath());
	    	fcc.setTransferId(frc.getTransferId());
	    	sendMessage(sendSocket, fcc);
	    	
	    	Transfer t = new Transfer();
	    	t.setFile(f);
	    	t.setId(fcc.getTransferId());
	    	t.setPeer(new Peer(frc.getAccountName()));
	    	t.setBlockSize(2048);
	    	t.setNumberOfBytesFinished(frc.getSentBytes());
	    	if (t.getNumberOfBytesFinished()>0)
	    		t.setState(TransferState.RESUMEDUPLOAD);
	    	else
	    		t.setState(TransferState.UPLOADING);
	    	
	    	if (PeerFacade.getInstance().getActiveUploads()<ClientConfigurationController.getInstance().getConfiguration().getMaximumUploads()) {
		    	PeerFacade.getInstance().addToUploads(t);
		    	if (t.getState()==TransferState.UPLOADING)
		    		EventController.getInstance().triggerDownloadStartedEvent(t);
		    	
		    	YourTurnCommando ytc = new YourTurnCommando();
		    	ytc.setAccountName(PeerFacade.getInstance().getPeer().getAccountName());
		    	ytc.setSize(f.length());
		    	ytc.setBlockSize(t.getBlockSize());
		    	ytc.setFileName(frc.getFileName());
		    	ytc.setPath(frc.getPath());
		    	ytc.setTransferId(frc.getTransferId());
		    	sendMessage(sendSocket, ytc);
	    	}

	    	

		} else {
			FileNotFoundCommando fnfc = new FileNotFoundCommando();
			fnfc.setAccountName(PeerFacade.getInstance().getPeer().getAccountName());
	    	fnfc.setFileName(frc.getFileName());
	    	fnfc.setPath(frc.getPath());
	    	fnfc.setTransferId(frc.getTransferId());
	    	sendMessage(sendSocket, fnfc);
		}
	}

	private void handleYourTurnCommando(Socket sendSocket, YourTurnCommando ytc) {			
		MyTurnCommando mtc = new MyTurnCommando();
		mtc.setAccountName(PeerFacade.getInstance().getDownloadTransferForId(ytc.getTransferId()).getPeer().getAccountName());
		mtc.setFileName(ytc.getFileName());
		mtc.setPath(ytc.getPath());
		mtc.setTransferId(ytc.getTransferId());
		sendMessage(sendSocket, mtc);
		
		Transfer t = PeerFacade.getInstance().getDownloadTransferForId(ytc.getTransferId());
		t.setFileSize(ytc.getSize());
		t.setBlockSize(ytc.getBlockSize());
		
	}

	private void handleFileConfirmCommando(Socket sendSocket, FileConfirmCommando fcc) {
		// TODO Auto-generated method stub
		
	}

	private void handleFileNotFoundCommando(Socket sendSocket, FileNotFoundCommando fnfc) {
		Transfer current = PeerFacade.getInstance().getDownloadTransferForId(fnfc.getTransferId());
		current.setState(TransferState.FAILED);
		EventController.getInstance().triggerDownloadFailedEvent(current);
	}
	
	public void sendMessage(Socket sendSocket, Commando commando) {
		System.out.println("Verstuurd Commando");
		System.out.println("------------------");
		System.out.println(commando);
		System.out.println("");
		
		//System.out.println("[" +commando + "] wordt gezonden naar " + s.getInetAddress().getHostAddress() + " op poort " + s.getPort());

		try {
			printWriter = new PrintWriter(sendSocket.getOutputStream(), true);
			printWriter.println(commando.toString());

		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void activeDownloadsChanged() {
		// TODO Auto-generated method stub
		
	}

	public void activeUploadsChanged() {
		
	}
}
