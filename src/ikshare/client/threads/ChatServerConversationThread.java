package ikshare.client.threads;

import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.chat.ChatCommandoParser;
import ikshare.protocol.command.chat.ChatHasEnteredRoomCommando;
import ikshare.protocol.command.chat.ChatHasLeftRoomCommando;
import ikshare.protocol.command.chat.ChatInvalidRoomPasswordCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;
import ikshare.protocol.command.chat.ChatWelcomeCommando;
import ikshare.protocol.command.chat.ChatYouEnterRoomCommando;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Boris Martens
 */
public class ChatServerConversationThread implements Runnable
{
    private Socket serverConnection;
    private PrintWriter outputWriter;
    private BufferedReader incomingReader;
    private boolean running = false;
    
    public ChatServerConversationThread() throws IOException
    {    
        serverConnection = new Socket(
            InetAddress.getByName(ClientConfigurationController.getInstance().getConfiguration().getChatServerAddress()),
            ClientConfigurationController.getInstance().getConfiguration().getChatServerPort());
        outputWriter = new PrintWriter(serverConnection.getOutputStream(),true);
        incomingReader = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
        running = true;
    }
    
    public void run() {
        try {
            while (running) {
                String inputLine = incomingReader.readLine();
                System.out.println("[INC] " + inputLine);
                if (inputLine != null) {
                    Commando c = ChatCommandoParser.getInstance().parse(inputLine);
                    if(c instanceof ChatMessageCommando){
                    	EventController.getInstance().triggerReceivingMessage((ChatMessageCommando)c);
                    }
                    else if (c instanceof ChatWelcomeCommando) {
                    	EventController.getInstance().triggerYouLoggedOn((ChatWelcomeCommando)c);
                    }
                    else if (c instanceof ChatHasEnteredRoomCommando) {
                    	EventController.getInstance().triggerUserEnteringRoom((ChatHasEnteredRoomCommando)c);
                    }
                    else if (c instanceof ChatHasLeftRoomCommando) {
                    	EventController.getInstance().triggerUserLeavingRoom((ChatHasLeftRoomCommando)c);
                    }
                    else if (c instanceof ChatYouEnterRoomCommando){
                    	EventController.getInstance().triggerYouEnterRoom((ChatYouEnterRoomCommando)c);
                    }
                    else if (c instanceof ChatInvalidRoomPasswordCommando){
                    	EventController.getInstance().triggerInvalidRoomPassword((ChatInvalidRoomPasswordCommando)c);
                    }
                }
            }
            serverConnection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
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
