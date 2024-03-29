package ikshare.client.threads;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.CommandoParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class ServerConversationThread implements Runnable{

    private Socket serverConnection;
    private PrintWriter outputWriter;
    private BufferedReader incomingReader;
    private boolean running = false;
    
    public ServerConversationThread() throws IOException{
    
            serverConnection = new Socket(
                InetAddress.getByName(ClientConfigurationController.getInstance().getConfiguration().getIkshareServerAddress()),
                ClientConfigurationController.getInstance().getConfiguration().getIkshareServerPort());
            outputWriter = new PrintWriter(serverConnection.getOutputStream(),true);
            incomingReader = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
            running = true;
    
    }
    
    public void run() {
        try {
            while (running) {
                String inputLine = incomingReader.readLine();
                if (inputLine != null) {
                    Commando c = CommandoParser.getInstance().parse(inputLine);
	            EventController.getInstance().triggerCommandoReceivedEvent(c);
                }
                else{
                    running = false;
                    throw new SocketException();
                }
                
            }
            serverConnection.close();
        } catch (SocketException ex) {
            EventController.getInstance().triggerServerConnectionInterupted();
            running = false;
        }
        catch(Exception ex){
            EventController.getInstance().triggerServerConnectionInterupted();
            running = false;
        }
    }
    
    public void sendCommand(Commando command){
        if(running){
            outputWriter.println(command.toString());
        }
    }
    
    public void stop(){
        running = false;
    }
}
