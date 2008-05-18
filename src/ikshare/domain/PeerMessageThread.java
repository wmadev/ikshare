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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


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
		TransferController.getInstance().startDownloadThread(TransferController.getInstance().getDownloadTransferForId(gfic.getTransferId()));	
	}

	private void handleOtherCommandos(Commando c) {
		try {
			clientSocket.close();
		} catch (IOException e) {
			
		}
	}

	private void handleCancelTransferCommando(CancelTransferCommando ctc) {
		Transfer canceledTransfer = TransferController.getInstance().getUploadTransferForId(ctc.getTransferId());
		canceledTransfer.setState(TransferState.CANCELLEDUPLOAD);
		EventController.getInstance().triggerDownloadCanceledEvent(canceledTransfer);
		
		TransferController.getInstance().getPeerFileUploadThreadForTransfer(canceledTransfer).stop();
	}

	private void handlePauseTransferCommando(PauseTransferCommando ptc) {
		Transfer pausedTransfer = TransferController.getInstance().getUploadTransferForId(ptc.getTransferId());
		pausedTransfer.setState(TransferState.PAUSEDUPLOAD);
		EventController.getInstance().triggerDownloadPausedEvent(pausedTransfer);
		
		TransferController.getInstance().getPeerFileUploadThreadForTransfer(pausedTransfer).stop();
	}
	
	private void handleResumeTransferCommando(ResumeTransferCommando rtc) {
		Transfer resumedTransfer = TransferController.getInstance().getUploadTransferForId(rtc.getTransferId());
		resumedTransfer.setState(TransferState.RESUMEDUPLOAD);
		EventController.getInstance().triggerDownloadResumedEvent(resumedTransfer);
		
		TransferController.getInstance().startResumeThread(TransferController.getInstance().getDownloadTransferForId(rtc.getTransferId()));	
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
		gfic.setAccountName(TransferController.getInstance().getPeer().getAccountName());
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
	    	fcc.setAccountName(TransferController.getInstance().getPeer().getAccountName());
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
	    	TransferController.getInstance().addToUploads(t);
	    	
	    	
	    	if (TransferController.getInstance().getActiveUploads()<ClientConfigurationController.getInstance().getConfiguration().getMaximumUploads()) {
		    	
		    	if (t.getState()==TransferState.UPLOADING)
		    		EventController.getInstance().triggerDownloadStartedEvent(t);
		    	
		    	YourTurnCommando ytc = new YourTurnCommando();
		    	ytc.setAccountName(TransferController.getInstance().getPeer().getAccountName());
		    	ytc.setSize(f.length());
		    	ytc.setBlockSize(t.getBlockSize());
		    	ytc.setFileName(frc.getFileName());
		    	ytc.setPath(frc.getPath());
		    	ytc.setTransferId(frc.getTransferId());
		    	sendMessage(ytc);
	    	}

	    	

		} else {
			FileNotFoundCommando fnfc = new FileNotFoundCommando();
			fnfc.setAccountName(TransferController.getInstance().getPeer().getAccountName());
	    	fnfc.setFileName(frc.getFileName());
	    	fnfc.setPath(frc.getPath());
	    	fnfc.setTransferId(frc.getTransferId());
	    	sendMessage(fnfc);
		}
	}

	private void handleYourTurnCommando(YourTurnCommando ytc) {			
		MyTurnCommando mtc = new MyTurnCommando();
		mtc.setAccountName(TransferController.getInstance().getDownloadTransferForId(ytc.getTransferId()).getPeer().getAccountName());
		mtc.setFileName(ytc.getFileName());
		mtc.setPath(ytc.getPath());
		mtc.setTransferId(ytc.getTransferId());
		sendMessage(mtc);
		
		Transfer t = TransferController.getInstance().getDownloadTransferForId(ytc.getTransferId());
		t.setFileSize(ytc.getSize());
		t.setBlockSize(ytc.getBlockSize());
		
	}

	private void handleFileConfirmCommando(FileConfirmCommando fcc) {
		// TODO Auto-generated method stub
		
	}

	private void handleFileNotFoundCommando(FileNotFoundCommando fnfc) {
		Transfer current = TransferController.getInstance().getDownloadTransferForId(fnfc.getTransferId());
		current.setState(TransferState.FAILED);
		EventController.getInstance().triggerDownloadFailedEvent(current);
	}
	
	public void sendMessage(final Commando commando) {

		
		if (outputWriter!=null) {
			outputWriter.println(commando.toString());
			System.out.println("Verstuurd Commando");
			System.out.println("------------------");
			System.out.println(commando);
			System.out.println("");
		}
		/*
		
		ExecutorService es = Executors.newCachedThreadPool();
		es.execute(new Runnable() {

			public void run() {
				
				//System.out.println("[" +commando + "] wordt gezonden naar " + s.getInetAddress().getHostAddress() + " op poort " + s.getPort());
				
				try {

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
		*/
		
	}

	public void activeDownloadsChanged() {
		// TODO Auto-generated method stub
		
	}

	public void activeUploadsChanged() {
		// TODO Auto-generated method stub
		
	}
    
}
