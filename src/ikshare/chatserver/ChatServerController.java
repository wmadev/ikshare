package ikshare.chatserver;

import ikshare.chatserver.datatypes.ChatClient;
import ikshare.chatserver.datatypes.ChatRoom;
import java.util.ArrayList;

/**
 *
 * @author Boris martens
 */
public class ChatServerController {
    private ArrayList<ChatClient> onlineClients = new ArrayList<ChatClient>();
    private ArrayList<ChatRoom> rooms = new ArrayList<ChatRoom>();
    
    private static ChatServerController instance;
    
    public ChatServerController() {
    }
    
    public static ChatServerController getInstance()
    {
        if(instance==null)
            instance = new ChatServerController();
        return instance;
    }
    
    public void start()
    {
        
    }
    
    public void stop()
    {
        
    }
    
    public void ClientLogsIn(ChatClient client)
    {
        onlineClients.add(client);
    }
    
    public void ClientLogsOff(ChatClient client)
    {
        onlineClients.remove(client);
    }
    
    public ChatClient GetClientByName (String clientName)
    {
        ChatClient client = null;
        int counter = 0;
        while(client==null && counter < onlineClients.size())
        {
            if( onlineClients.get(counter).getNickName() == clientName)
                client = onlineClients.get(counter);
        }
        
        return client;
    }
}
