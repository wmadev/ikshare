package ikshare.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ikshare.server.threads.HandleClientThread;

/**
 *
 * @author Boris Martens
 */
public class ChatServer implements Runnable 
{ 
    private boolean running = false;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
       
    public ChatServer(){
        try 
        {
            serverSocket = new ServerSocket(6005);
            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(0);
            executorService = Executors.newCachedThreadPool();
            running = true;
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }

    public void run() 
    {
        try
        {
            while (running) 
            {
                Socket clientSocket  = serverSocket.accept();
                executorService.execute(new HandleClientThread(clientSocket));
            }
            serverSocket.close();
        } catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }

    public void stop() 
    {
        running = false;
    }
}
