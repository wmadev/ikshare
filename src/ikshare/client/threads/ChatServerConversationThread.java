package ikshare.client.threads;

import ikshare.client.ClientController;
import ikshare.client.configuration.ClientConfigurationController;
import ikshare.domain.event.EventController;
import ikshare.protocol.command.Commando;
import ikshare.protocol.command.chat.ChatCommandoParser;
import ikshare.protocol.command.chat.ChatHasEnteredRoomCommando;
import ikshare.protocol.command.chat.ChatHasLeftRoomCommando;
import ikshare.protocol.command.chat.ChatInvalidRoomPasswordCommando;
import ikshare.protocol.command.chat.ChatLogNiLukNiCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;
import ikshare.protocol.command.chat.ChatRoomDoesNotExistCommando;
import ikshare.protocol.command.chat.ChatUpdateRoomsListCommando;
import ikshare.protocol.command.chat.ChatWelcomeCommando;
import ikshare.protocol.command.chat.ChatYouEnterRoomCommando;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

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
    	System.out.println("Connecting to chatserver on: " + ClientConfigurationController.getInstance().getConfiguration().getChatServerAddress() + " : " +
            ClientConfigurationController.getInstance().getConfiguration().getChatServerPort());
    	
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
                    else if (c instanceof ChatUpdateRoomsListCommando) {
                    	EventController.getInstance().triggerRoomsUpdate((ChatUpdateRoomsListCommando)c);
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
                    else if (c instanceof ChatRoomDoesNotExistCommando){
                    	EventController.getInstance().triggerRoomDoesNotExist((ChatRoomDoesNotExistCommando)c);
                    }
                    else if (c instanceof ChatInvalidRoomPasswordCommando){
                    	EventController.getInstance().triggerInvalidRoomPassword((ChatInvalidRoomPasswordCommando)c);
                    }
                    else if (c instanceof ChatLogNiLukNiCommando){
                    	EventController.getInstance().triggerLogNiLukNi((ChatLogNiLukNiCommando)c);
                    }
                }
                else
                {
                    throw new SocketException();
                }
            }
            serverConnection.close();
        } 
        catch (SocketException se)
        {
        	EventController.getInstance().triggerChatServerInterupt("connection interupted");
        	ClientController.getInstance().stopChatServerConversation();
        }
        catch (Exception ex) 
        {
            try
            {
                serverConnection.close();
            }
            catch(IOException e)
            {
                
            }
            ex.printStackTrace();
            ClientController.getInstance().stopChatServerConversation();
        }
    }
    
    public void sendCommand(Commando command){
        if(running){
            System.out.println("[OUT] "+command.toString());
            outputWriter.println(command.toString());
        }
    }
    
    public void stop(){
        running = false;
    }
}
