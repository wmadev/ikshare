package ikshare.chatserver;

import ikshare.chatserver.datatypes.ChatClient;
import ikshare.chatserver.datatypes.ChatRoom;
import ikshare.protocol.command.chat.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        
        ChatRoom iKShareRoom = new ChatRoom();
        iKShareRoom.setVisible(true);
        iKShareRoom.setPersistant(true);
        iKShareRoom.setRoomName("iKShare");
        rooms.add(iKShareRoom);
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
    		ClientLeavesRoom(currentroom.getRoomName(), client);
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
            
            counter++;
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

	public void ClientEntersRoom(String roomName, String password, ChatClient client) 
	{
		ChatRoom room = GetRoomByName(roomName);
		
		if(room!=null && !room.HasClient(client))
		{
			if(room.getPassword()!= null && !room.getPassword().equals("") && !room.getPassword().equals(password))
			{
				ChatInvalidRoomPasswordCommando CIRPCommando = new ChatInvalidRoomPasswordCommando();
				CIRPCommando.setRoomName(roomName);
				client.getThread().SendMessage(CIRPCommando);
			}
			else
			{
				System.out.println( ">> " + client.getNickName() + " enters room " + roomName + ".");
				
				room.AddClientToRoom(client);
				
				ChatYouEnterRoomCommando CYERCommando = new ChatYouEnterRoomCommando();
				CYERCommando.setRoomName(roomName);
				for(ChatClient member : room.getClients())
					CYERCommando.addRoomMember(member.getNickName());
				
				client.getThread().SendMessage(CYERCommando);
			}
		}
		else if (room == null) // room does not exist
		{
			ChatRoomDoesNotExistCommando CRDNECommando = new ChatRoomDoesNotExistCommando();
			CRDNECommando.setRoomName(roomName);
			client.getThread().SendMessage(CRDNECommando);
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
		{
			if(ChatServer.debug)
				System.out.print("Room is empty, removing room...");
			
			rooms.remove(room);
			
			if(room.isVisible())
				UpdateRoomsList(roomName, false);
			
			if(ChatServer.debug)
				System.out.println(" gone!");
		}
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

	public void CreateRoom(ChatCreateRoomCommando command, ChatClient client) 
	{
		ChatRoom foundRoom = GetRoomByName(command.getRoomName());
		
		if(foundRoom==null)
		{
                    if(CheckRoomName(command.getRoomName()))
                    {
			System.out.println( ">> " + client.getNickName() + " creates room " + command.getRoomName() + ".");
			ChatRoom newRoom = new ChatRoom();
			newRoom.setRoomName(command.getRoomName());
			if(command.getPassword()!=null && !command.getPassword().equals("") && CheckRoomName(command.getPassword()))
				newRoom.setPassword(command.getPassword());
			else
				newRoom.setPassword("");
			newRoom.setVisible(!command.isPrivateRoom());
			newRoom.setPersistant(false);
			rooms.add(newRoom);
			
			if(newRoom.isVisible())
				UpdateRoomsList(command.getRoomName(), true);
			
			ClientEntersRoom(command.getRoomName(), command.getPassword(), client);
                    }
		}
	}
	
    private boolean CheckRoomName(String roomName)
    {
        boolean nameAcceptable = true;

        if(roomName==null || roomName.length() < 1 || roomName.length() > 32)
            nameAcceptable = false;
        
        return nameAcceptable;
    }
        
	public void UpdateRoomsList(String roomName, boolean Added)
	{
		ChatUpdateRoomsListCommando CURLCommando = new ChatUpdateRoomsListCommando();
		CURLCommando.setAdded(Added);
		CURLCommando.setRoomName(roomName);
		
		for(ChatClient client : onlineClients)
		{
			client.getThread().SendMessage(CURLCommando);
		}
	}
}
