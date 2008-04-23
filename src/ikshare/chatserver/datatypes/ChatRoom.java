package ikshare.chatserver.datatypes;

import ikshare.protocol.command.*;
import ikshare.protocol.command.chat.*;

import java.util.ArrayList;

/**
 *
 * @author Boris martens
 */
public class ChatRoom 
{
    private ArrayList<ChatClient> clients = new ArrayList<ChatClient>();
    private String roomName;
    //private boolean visible = true;
    //private String password;
    
    public ChatRoom() 
    {
    }
    
	public String getRoomName() 
	{
		return roomName;
	}

	public void setRoomName(String roomName) 
	{
		this.roomName = roomName;
	}
    
    public int getNumberOfClients()
    {
        return clients.size();
    }
    
    public boolean AddClientToRoom(ChatClient client)
    {
    	ChatHasEnteredRoomCommando command = new ChatHasEnteredRoomCommando();
    	command.setNickName(client.getNickName());
    	command.setRoomName(roomName);
    	BroadCast(command);
    	
        return clients.add(client);
    }
    
    public boolean RemoveClientFromRoom(ChatClient client)
    {
    	boolean worked = clients.remove(client);
    	
    	if (worked)
    	{
	    	ChatHasLeftRoomCommando command = new ChatHasLeftRoomCommando();
	    	command.setNickName(client.getNickName());
	    	command.setRoomName(roomName);
	    	BroadCast(command);
    	}
    	
        return worked;
    }
    
    public boolean HasClient(ChatClient client)
    {
    	return (clients.contains(client));
    }
    
    public void BroadCast(Commando command)
    {
        for(ChatClient client : clients)
        {
        	if(command instanceof ChatMessageCommando && 
        			!client.getNickName().equals(((ChatMessageCommando)command).getSender()))
        		client.SendMessage(command);
        }
    }
}
