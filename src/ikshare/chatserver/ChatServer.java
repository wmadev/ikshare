package ikshare.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer implements Runnable 
{ 
	private static int SERVER_PORT = 6005;
	public static boolean debug = false;
	
    private boolean running = false;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
       
    public ChatServer(){
        try 
        {
        	System.out.print("Starting chat server on port " + SERVER_PORT + "... ");
            serverSocket = new ServerSocket(SERVER_PORT);
            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(0);
            executorService = Executors.newCachedThreadPool();
            running = true;
            System.out.println("started.");
        } 
        catch (IOException ex) 
        {
        	System.out.println("exception!");
        	if(debug)
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
                executorService.execute(new HandleChatClientThread(clientSocket));
            }
            if(serverSocket!=null)
            	serverSocket.close();
        } 
        catch (IOException ex){
        	if(debug)
            	ex.printStackTrace();
        }
    }

    public void stop() 
    {
        running = false;
    }
}
