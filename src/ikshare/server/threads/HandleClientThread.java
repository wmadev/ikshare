/**
 * This class represents a thread that handles a client that connects with the server.
 */
package ikshare.server.threads;

import java.io.*;
import java.net.Socket;
/**
 *
 * @author awosy
 */
public class HandleClientThread implements Runnable{
    private Socket clientSocket;
    private boolean running = false;
    private PrintWriter outputWriter;
    private BufferedReader incomingReader;
    
    public HandleClientThread(Socket socket){
        try {
            clientSocket = socket;
            outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            incomingReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            running = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void run() {
        try {
            while(running){
                String inputLine = incomingReader.readLine();
                if (inputLine != null) {
                    System.out.println("Client sent: " + inputLine);
                    if (inputLine.trim().equals("-1")) {
                        System.out.println("Closing client connection");
                        outputWriter.close();
                        incomingReader.close();
                        clientSocket.close();
                        running = false;
                    }
                }
            }
            clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
