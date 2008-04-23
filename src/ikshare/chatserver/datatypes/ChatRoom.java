package ikshare.chatserver.datatypes;

import java.util.ArrayList;

/**
 *
 * @author Boris martens
 */
public class ChatRoom 
{
    private ArrayList<ChatClient> clients = new ArrayList<ChatClient>();
    private boolean visible = true;
    private String password;
    
    public ChatRoom() 
    {
    }
    
    public int getNumberOfClients()
    {
        return clients.size();
    }
    
    public boolean AddClientToRoom(ChatClient client)
    {
        return clients.add(client);
    }
    
    public boolean RemoveClientFromRoom(ChatClient client)
    {
        return clients.remove(client);
    }
    
    public void BroadCast(ChatMessage message)
    {
        for(ChatClient client : clients)
        {
            client.SendMessage(message);
        }
    }
}
