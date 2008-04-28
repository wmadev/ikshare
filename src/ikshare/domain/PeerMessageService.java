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
import ikshare.server.threads.HandleClientThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerMessageService implements Runnable{
    private ServerSocket messageServer;
    private boolean running;
	private int port;
	private ExecutorService executorService;
	
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
    	
    	executorService = Executors.newCachedThreadPool();
    }

	public void run() {
		while (running) {
			Socket clientSocket = null;
			try {
				System.out.println("Listening to incoming client connections...");
				while (running) {
					clientSocket = messageServer.accept();
					System.out.println("Received connection with client, starting seperate thread...");
					executorService.execute(new PeerMessageThread(clientSocket));
				}
				messageServer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (clientSocket != null)
					try {
						clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
	}

	public void stop() {
		running = false;
	}
}
