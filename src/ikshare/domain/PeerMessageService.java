package ikshare.domain;

import ikshare.domain.event.EventController;
import ikshare.domain.event.listener.FileTransferListener;
import ikshare.protocol.command.CancelTransferCommando;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CommandoParser;
import ikshare.protocol.command.FileConfirmCommando;
import ikshare.protocol.command.FileNotFoundCommando;
import ikshare.protocol.command.FileRequestCommando;
import ikshare.protocol.command.FoundCommando;
import ikshare.protocol.command.FoundItAllCommando;
import ikshare.protocol.command.GetConnCommando;
import ikshare.protocol.command.GiveConnCommando;
import ikshare.protocol.command.GivePeerCommando;
import ikshare.protocol.command.MyTurnCommando;
import ikshare.protocol.command.PassTurnCommando;
import ikshare.protocol.command.WelcomeCommando;
import ikshare.protocol.command.YourTurnCommando;
import ikshare.protocol.exception.CommandNotFoundException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerMessageService extends Thread implements Runnable, FileTransferListener{
    private Socket sendSocket;
    private ServerSocket messageServer;
    private PrintWriter printWriter;
    private boolean running;
	private int port;
	public PeerMessageService() {
    	running = true;
    	port = 6000;
    	try {
			messageServer = new ServerSocket(port);
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
	            Socket replySocket = new Socket(link.getInetAddress(), 6000);

	            
	            // Step 2. Set up input stream
	            in = new BufferedReader(new
	                InputStreamReader(link.getInputStream()));
	            
	            // Step 3. Receive data
	            String input = in.readLine();
	            
	            while (input != null)
	            {
	            		System.out.println(input);
		                Commando c = null;
		                try {
		                	c = CommandoParser.getInstance().parse(input);
		                } catch (CommandNotFoundException cnfe) {
		                	
		                }
		                System.out.println(c);
		                handleCommando(replySocket, c);
		                input = in.readLine();
	            }
	            link.close();
	            replySocket.close();
	        } catch (Exception e) {
	        	//TODO
	        }
		}
		
	}

	private void handleCommando(Socket replySocket, Commando c) {
		if (c instanceof FoundCommando) {
			//TODO handle FoundCommando
		}
		else if (c instanceof FoundItAllCommando) {
			//TODO handle FoundItAllCommando
		}
		else if (c instanceof GivePeerCommando) {
			//TODO handle GivePeerCommando
			
		}
		else if (c instanceof FileRequestCommando) {
			File f = new File(((FileRequestCommando)c).getPath().replace('\\', '/')+((FileRequestCommando)c).getFileName());
         
			System.out.println(f.getPath());
			
			if( f.exists() ) {
		    	FileConfirmCommando fcc = new FileConfirmCommando();
		    	fcc.setAccountName("Monet");
		    	fcc.setFileName(((FileRequestCommando)c).getFileName());
		    	fcc.setPath(((FileRequestCommando)c).getPath());
		    	sendMessage(replySocket, fcc);
		    	
		    	Transfer t = new Transfer();
		    	t.setFile(new File(fcc.getPath()+"/"+fcc.getFileName()));
		    	t.setId(new Date().getTime()+"");
		    	t.setPeer(new Peer(fcc.getAccountName(), null));
		    	t.setState(TransferState.DOWNLOADING);
		    	PeerFacade.getInstance().addToUploads(new Transfer());
		    	EventController.getInstance().triggerDownloadStartedEvent(t);
		    	
		    	
		    	YourTurnCommando ytc = new YourTurnCommando();
		    	ytc.setAccountName("Monet");
		    	ytc.setBlocks(4000);
		    	ytc.setBlockSize(2048);
		    	ytc.setFileName(((FileRequestCommando)c).getFileName());
		    	ytc.setPath(((FileRequestCommando)c).getPath());
		    	sendMessage(replySocket, ytc);
			} else {
				FileNotFoundCommando fnfc = new FileNotFoundCommando();
				fnfc.setAccountName("Monet");
		    	fnfc.setFileName(((FileRequestCommando)c).getFileName());
		    	fnfc.setPath(((FileRequestCommando)c).getPath());
		    	sendMessage(replySocket, fnfc);
			}
		}
		else if (c instanceof FileConfirmCommando) {
			FileConfirmCommando fcc = (FileConfirmCommando) c;
			Transfer t = new Transfer();
			t.setFile(new File(fcc.getPath()+"/"+fcc.getFileName()));
			t.setId(new Date().getTime()+"");
			t.setPeer(new Peer(fcc.getAccountName(), null));
			t.setState(TransferState.UPLOADING);
			PeerFacade.getInstance().addToUploads(new Transfer());
			EventController.getInstance().triggerDownloadStartedEvent(t);
		}
		else if (c instanceof FileNotFoundCommando) {
			//TODO handle FileNotFoundCommando
		}
		else if (c instanceof YourTurnCommando) {
			MyTurnCommando mtc = new MyTurnCommando();
			mtc.setAccountName("Degas");
			mtc.setFileName(((YourTurnCommando)c).getFileName());
			mtc.setPath(((YourTurnCommando)c).getPath());
			sendMessage(replySocket, mtc);
			//PeerFacade.getInstance().startDownloadThread();
		}
		else if (c instanceof MyTurnCommando) {
			//TODO handle MyTurnCommando
			
		}
		else if (c instanceof PassTurnCommando) {
			//TODO handle PassTurnCommando
		}
		else if (c instanceof GetConnCommando) {
			GiveConnCommando gcc = new GiveConnCommando();
			gcc.setPort(6002);
			sendMessage(replySocket, gcc);
		}
		else if (c instanceof GiveConnCommando) {
			//TODO handle GiveConnCommando
		}
		else if (c instanceof CancelTransferCommando) {
			Transfer t = new Transfer();
			t.setId(((CancelTransferCommando)c).getTransferId());
			EventController.getInstance().triggerDownloadCanceledEvent(t);
		}

	}
	
	public void sendMessage(Socket s, Commando commando) {
		System.out.println("[" +commando + "] wordt gezonden naar " + s.getInetAddress().getHostAddress() + " op poort " + s.getPort());
		
		sendSocket = s;
		try {
			printWriter = new PrintWriter(sendSocket.getOutputStream(), true);
			printWriter.println(commando.toString());

		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void transferCanceled(Transfer transfer) {
		CancelTransferCommando ctc = new CancelTransferCommando();
		ctc.setTransferId("");
		try {
			sendMessage(new Socket(InetAddress.getByName("192.168.1.3"), 6000), ctc);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void transferFailed(Transfer transfer) {
		// TODO Auto-generated method stub
		
	}

	public void transferFinished(Transfer transfer) {
		// TODO Auto-generated method stub
		
	}

	public void transferStarted(Transfer transfer) {
		// TODO Auto-generated method stub
		
	}

	public void transferStateChanged(Transfer transfer) {
		// TODO Auto-generated method stub
		
	}

	public void transferStopped(Transfer transfer) {
		// TODO Auto-generated method stub
		
	}

}
