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
    private boolean visible = true;
    private String password;
    private boolean persistant = false;
    
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
    
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    public int getNumberOfClients()
    {
        return clients.size();
    }

	public boolean isPersistant() {
		return persistant;
	}

	public void setPersistant(boolean persistant) {
		this.persistant = persistant;
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
    	boolean contains = false;
    	int counter = 0;
    	
    	while(!contains && counter < clients.size()){
    		if(client.getNickName().equals(clients.get(counter).getNickName()))
    			contains = true;
    		counter++;
    	}
    	
    	return contains;
    }
    
    public void BroadCast(Commando command)
    {
        for(ChatClient client : clients)
        {
        	if(!(command instanceof ChatMessageCommando 
        			&& client.getNickName().equals(((ChatMessageCommando)command).getSender())))
        		client.SendMessage(command);
        }
    }

	public ArrayList<ChatClient> getClients() {
		return clients;
	}

	public void setClients(ArrayList<ChatClient> clients) {
		this.clients = clients;
	}
}
