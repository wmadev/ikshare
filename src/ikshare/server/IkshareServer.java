/*
 * This class is a representation of the central ikshare server.
 */

package ikshare.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ikshare.server.threads.HandleClientThread;

public class IkshareServer implements Runnable {
       
    private boolean running = false;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
       
    public IkshareServer(){
        System.out.println("Starting iKshare server...");
        try {
            serverSocket = new ServerSocket(6000);
            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(0);
            //readBuffer = new byte[512];
            executorService = Executors.newCachedThreadPool();
            running = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        try {
            
            System.out.println("Listening to incoming client connections...");
            while (running) {
                Socket clientSocket  = serverSocket.accept();
                System.out.println("Received connection with client, starting seperate thread...");
                executorService.execute(new HandleClientThread(clientSocket));
            }
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        running = false;
    }
}
