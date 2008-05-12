/**
 * This class represents a thread that handles a client that connects with the server.
 */
package ikshare.domain;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.TransferQueueListener;
import ikshare.protocol.command.*;
import ikshare.server.*;
import ikshare.server.data.*;
import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author awosy
 */
public class PeerMessageThread implements Runnable, TransferQueueListener{
    private Socket clientSocket;
    private boolean running = false;
    private PrintWriter outputWriter;
    private BufferedReader incomingReader;
    private Transfer transfer;
    public PeerMessageThread(Transfer transfer){
    	EventController.getInstance().addTransferQueueListener(this);
    	
    	this.transfer = transfer;
        running = true;
    }
    
    public PeerMessageThread(Socket socket){
    	EventController.getInstance().addTransferQueueListener(this);
    	clientSocket = socket;
    	running = true;
    }

	public void run() {
        try {
        	if (clientSocket == null)
        		clientSocket = new Socket(transfer.getPeer().getInternetAddress(), transfer.getPeer().getPort());
       
       		outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
          	incomingReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        	
			
            while(running){
                String inputLine = incomingReader.readLine();
                while (inputLine != null) {
                    Commando c = CommandoParser.getInstance().parse(inputLine);
                    handleCommando(c);
                    inputLine = incomingReader.readLine();
                }
            }
            clientSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    
	private void handleCommando(Commando c) {
		System.out.println("Ontvangen Commando");
		System.out.println("------------------");
		System.out.println(c);
		System.out.println("");
		
		if (c instanceof FoundItAllCommando) {
			handleFoundItAllCommando((FoundItAllCommando) c);
		}
		else if (c instanceof GivePeerCommando) {
			handleGivePeerCommando((GivePeerCommando) c);
		}
		else if (c instanceof FileRequestCommando) {
			handleFileRequestCommando((FileRequestCommando) c);
		}
		else if (c instanceof FileConfirmCommando) {
			handleFileConfirmCommando((FileConfirmCommando) c);
		}
		else if (c instanceof FileNotFoundCommando) {
			handleFileNotFoundCommando((FileNotFoundCommando) c);
		}
		else if (c instanceof YourTurnCommando) {
			handleYourTurnCommando((YourTurnCommando) c);
		}
		else if (c instanceof MyTurnCommando) {
			handleMyTurnCommando((MyTurnCommando) c);
		}
		else if (c instanceof PassTurnCommando) {
			handlePassTurnCommando((PassTurnCommando) c);
		}
		else if (c instanceof GoForItCommando) {
			handleGoForItCommando((GoForItCommando) c);
		}
		else if (c instanceof GetConnCommando) {
			handleGetConnCommando((GetConnCommando) c);
		}
		else if (c instanceof GiveConnCommando) {
			handleGiveConnCommando((GiveConnCommando) c);
		}
		else if (c instanceof CancelTransferCommando) {
			handleCancelTransferCommando((CancelTransferCommando) c);
		}
		else if (c instanceof PauseTransferCommando) {
			handlePauseTransferCommando((PauseTransferCommando) c);
		}
		else if (c instanceof ResumeTransferCommando) {
			handleResumeTransferCommando((ResumeTransferCommando) c);
		}
		else if(c instanceof Commando) {
			handleOtherCommandos(c);
		}

	}



	private void handleGoForItCommando(GoForItCommando gfic) {
		Transfer t = PeerFacade.getInstance().getDownloadTransferForId(gfic.getTransferId());
		PeerFacade.getInstance().startDownloadThread(PeerFacade.getInstance().getDownloadTransferForId(gfic.getTransferId()));	

	}

	private void handleOtherCommandos(Commando c) {
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleCancelTransferCommando(CancelTransferCommando ctc) {
		Transfer canceledTransfer = PeerFacade.getInstance().getUploadTransferForId(ctc.getTransferId());
		canceledTransfer.setState(TransferState.CANCELLEDUPLOAD);
		EventController.getInstance().triggerDownloadCanceledEvent(canceledTransfer);
		
		PeerFacade.getInstance().getPeerFileUploadThreadForTransfer(canceledTransfer).stop();
	}

	private void handlePauseTransferCommando(PauseTransferCommando ptc) {
		Transfer pausedTransfer = PeerFacade.getInstance().getUploadTransferForId(ptc.getTransferId());
		pausedTransfer.setState(TransferState.PAUSEDUPLOAD);
		EventController.getInstance().triggerDownloadPausedEvent(pausedTransfer);
		
		PeerFacade.getInstance().getPeerFileUploadThreadForTransfer(pausedTransfer).stop();
	}
	
	private void handleResumeTransferCommando(ResumeTransferCommando rtc) {
		Transfer resumedTransfer = PeerFacade.getInstance().getUploadTransferForId(rtc.getTransferId());
		resumedTransfer.setState(TransferState.RESUMEDUPLOAD);
		EventController.getInstance().triggerDownloadResumedEvent(resumedTransfer);
		
		PeerFacade.getInstance().startResumeThread(PeerFacade.getInstance().getDownloadTransferForId(rtc.getTransferId()));	
	}

	private void handleGiveConnCommando(GiveConnCommando givecc) {
		// TODO handleGiveConnCommando
		
	}

	private void handleGetConnCommando(GetConnCommando getcc) {
		// TODO handleGetConnCommando
	}

	private void handlePassTurnCommando(PassTurnCommando ptc) {
		// TODO handlePassTurnCommando
		
	}

	private void handleMyTurnCommando(MyTurnCommando mtc) {
		GoForItCommando gfic = new GoForItCommando();
		gfic.setAccountName(PeerFacade.getInstance().getPeer().getAccountName());
		gfic.setTransferId(mtc.getTransferId());
		sendMessage(gfic);		
	}

	private void handleGivePeerCommando(GivePeerCommando gpc) {
		// TODO handleGivePeerCommando
		
	}

	private void handleFoundItAllCommando(FoundItAllCommando fiac) {
		// TODO handleFoundItallCommando
		
	}

	private void handleFileRequestCommando(FileRequestCommando frc) {
		//File f = new File(frc.getPath().replace('\\', '/')+frc.getFileName());
		IKShareFile f = new IKShareFile(frc.getPath(),frc.getFileName());
		
		if( f.exists() ) {
	    	FileConfirmCommando fcc = new FileConfirmCommando();
	    	fcc.setAccountName(PeerFacade.getInstance().getPeer().getAccountName());
	    	fcc.setFileName(frc.getFileName());
	    	fcc.setPath(frc.getPath());
	    	fcc.setTransferId(frc.getTransferId());
	    	sendMessage(fcc);
	    	
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
	    	PeerFacade.getInstance().addToUploads(t);
	    	
	    	
	    	if (PeerFacade.getInstance().getActiveUploads()<ClientConfigurationController.getInstance().getConfiguration().getMaximumUploads()) {
		    	
		    	if (t.getState()==TransferState.UPLOADING)
		    		EventController.getInstance().triggerDownloadStartedEvent(t);
		    	
		    	YourTurnCommando ytc = new YourTurnCommando();
		    	ytc.setAccountName(PeerFacade.getInstance().getPeer().getAccountName());
		    	ytc.setSize(f.length());
		    	ytc.setBlockSize(t.getBlockSize());
		    	ytc.setFileName(frc.getFileName());
		    	ytc.setPath(frc.getPath());
		    	ytc.setTransferId(frc.getTransferId());
		    	sendMessage(ytc);
	    	}

	    	

		} else {
			FileNotFoundCommando fnfc = new FileNotFoundCommando();
			fnfc.setAccountName(PeerFacade.getInstance().getPeer().getAccountName());
	    	fnfc.setFileName(frc.getFileName());
	    	fnfc.setPath(frc.getPath());
	    	fnfc.setTransferId(frc.getTransferId());
	    	sendMessage(fnfc);
		}
	}

	private void handleYourTurnCommando(YourTurnCommando ytc) {			
		MyTurnCommando mtc = new MyTurnCommando();
		mtc.setAccountName(PeerFacade.getInstance().getDownloadTransferForId(ytc.getTransferId()).getPeer().getAccountName());
		mtc.setFileName(ytc.getFileName());
		mtc.setPath(ytc.getPath());
		mtc.setTransferId(ytc.getTransferId());
		sendMessage(mtc);
		
		Transfer t = PeerFacade.getInstance().getDownloadTransferForId(ytc.getTransferId());
		t.setFileSize(ytc.getSize());
		t.setBlockSize(ytc.getBlockSize());
		
	}

	private void handleFileConfirmCommando(FileConfirmCommando fcc) {
		// TODO Auto-generated method stub
		
	}

	private void handleFileNotFoundCommando(FileNotFoundCommando fnfc) {
		Transfer current = PeerFacade.getInstance().getDownloadTransferForId(fnfc.getTransferId());
		current.setState(TransferState.FAILED);
		EventController.getInstance().triggerDownloadFailedEvent(current);
	}
	
	public void sendMessage(final Commando commando) {

		/*
		if (outputWriter!=null) {
			outputWriter.println(commando.toString());
			System.out.println("Verstuurd Commando");
			System.out.println("------------------");
			System.out.println(commando);
			System.out.println("");
		}
		*/
		
		ExecutorService es = Executors.newCachedThreadPool();
		es.execute(new Runnable() {

			public void run() {
				
				//System.out.println("[" +commando + "] wordt gezonden naar " + s.getInetAddress().getHostAddress() + " op poort " + s.getPort());
				
				try {
					Thread.sleep(500);
					
					if (clientSocket == null) 
						clientSocket = new Socket(transfer.getPeer().getInternetAddress(), transfer.getPeer().getPort());

			        
			    	outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			        incomingReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

					System.out.println("Verstuurd Commando");
					System.out.println("------------------");
					System.out.println(commando);
					System.out.println("");
				} catch (Exception e) {
					
				}
			}
		});
		
		
	}

	public void activeDownloadsChanged() {
		// TODO Auto-generated method stub
		
	}

	public void activeUploadsChanged() {
		// TODO Auto-generated method stub
		
	}
    
}
