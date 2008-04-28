package ikshare.chatserver;

import ikshare.chatserver.datatypes.ChatClient;
import ikshare.chatserver.datatypes.ChatRoom;
import ikshare.protocol.command.chat.ChatMessageCommando;
import ikshare.protocol.command.chat.ChatYouEnterRoomCommando;

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
        ChatRoom helpRoom = new ChatRoom();
        helpRoom.setVisible(true);
        helpRoom.setPersistant(true);
        helpRoom.setRoomName("Help");
        rooms.add(helpRoom);
        
        for(int i = 1; i<5; i++)
        {
        	ChatRoom testRoom = new ChatRoom();
        	testRoom.setVisible(true);
        	testRoom.setPersistant(true);
        	testRoom.setRoomName("test room " + i);
        	rooms.add(testRoom);
        }

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
		System.out.println( ">> " + client.getNickName() + " logs on.");
		
        onlineClients.add(client);
    }
    
    public void ClientLogsOff(ChatClient client)
    {
    	System.out.println( ">> " + client.getNickName() + " logs off.");
    	
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
            if( onlineClients.get(counter).getNickName().equals(clientName))
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
            if( rooms.get(counter).getRoomName().equals(roomName))
                room = rooms.get(counter);
            
            counter++;
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
		
		if(!room.HasClient(client))
		{
			System.out.print( ">> room " + roomName + " has users: ");
			for(ChatClient chClient : room.getClients())
			{
				System.out.print(chClient.getNickName() + " ");
			}
			System.out.println();
			System.out.println( ">> " + client.getNickName() + " enters room " + roomName + ".");
			
			room.AddClientToRoom(client);
			
			ChatYouEnterRoomCommando CYERCommando = new ChatYouEnterRoomCommando();
			CYERCommando.setRoomName(roomName);
			for(ChatClient member : room.getClients())
				CYERCommando.addRoomMember(member.getNickName());
			
			client.getThread().SendMessage(CYERCommando);
		}
	}

	public void ClientLeavesRoom(String roomName, ChatClient client) 
	{
		System.out.println( ">> " + client.getNickName() + " leaves room " + roomName + ".");
		
		ChatRoom room = GetRoomByName(roomName);
		
		if(room != null)
		{
			room.RemoveClientFromRoom(client);
		}
		
		if(room.getNumberOfClients()==0 && !room.isPersistant())
			rooms.remove(room);
	}
	
	public ArrayList<ChatRoom> getPublicRooms()
	{
		ArrayList<ChatRoom> publicRooms = new ArrayList<ChatRoom>();
		
		for(ChatRoom room : rooms)
		{
			if(room.isVisible())
				publicRooms.add(room);
		}
		
		return publicRooms;
	}
}
