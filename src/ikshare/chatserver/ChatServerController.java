package ikshare.chatserver;

import ikshare.chatserver.datatypes.ChatClient;
import ikshare.chatserver.datatypes.ChatRoom;
import ikshare.protocol.command.chat.ChatMessageCommando;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Boris martens
 */
public class ChatServerController {
	private ChatServer server;
	private ExecutorService executorService;
	
    private ArrayList<ChatClient> onlineClients = new ArrayList<ChatClient>();
    private ArrayList<ChatRoom> rooms = new ArrayList<ChatRoom>();
    
    private static ChatServerController instance;
    
    public ChatServerController() {
    	executorService = Executors.newFixedThreadPool(1);
        server = new ChatServer();
    }
    
    public static ChatServerController getInstance()
    {
        if(instance==null)
            instance = new ChatServerController();
        return instance;
    }
    
    public void start()
    {
    	executorService.execute(server);
    }
    
    public void stop()
    {
    	server.stop();
    	executorService.shutdown();
    }
    
    public void ClientLogsIn(ChatClient client)
    {
        onlineClients.add(client);
    }
    
    public void ClientLogsOff(ChatClient client)
    {
    	ArrayList<ChatRoom> clientsrooms = GetRoomsByClient(client);
    	
    	for(ChatRoom currentroom : clientsrooms)
    	{
    		currentroom.RemoveClientFromRoom(client);
    	}
    	
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
    
    public ChatRoom GetRoomByName (String roomName)
    {
    	ChatRoom room = null;
        int counter = 0;
        while(room==null && counter < rooms.size())
        {
            if( rooms.get(counter).getRoomName() == roomName)
                room = rooms.get(counter);
        }
        
        return room;
    }
    
    public ArrayList<ChatRoom> GetRoomsByClient(ChatClient client)
    {
    	ArrayList<ChatRoom> clientsRooms = new ArrayList<ChatRoom>();
    	
    	for(ChatRoom currentroom : rooms)
    	{
    		if(currentroom.HasClient(client))
    			clientsRooms.add(currentroom);
    	}
    	
    	return clientsRooms;
    }

	public void ProcessMessage(ChatMessageCommando command) 
	{
    	if(!command.isPrivateMessage())
    	{
    		ChatRoom room = GetRoomByName(command.getRecipient());
    		if(room!=null)
    		{
    			room.BroadCast(command);
    		}
    	}
	}

	public void ClientEntersRoom(String roomName, ChatClient client) 
	{
		ChatRoom room = GetRoomByName(roomName);
		
		if(room == null)
		{
			room = new ChatRoom();
			room.setRoomName(roomName);
			rooms.add(room);
		}
		
		room.AddClientToRoom(client);
	}

	public void ClientLeavesRoom(String roomName, ChatClient client) 
	{
		ChatRoom room = GetRoomByName(roomName);
		
		if(room != null)
		{
			room.RemoveClientFromRoom(client);
		}
		
		if(room.getNumberOfClients()==0)
			rooms.remove(room);
	}
}
