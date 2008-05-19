package ikshare.protocol.command.chat;

import java.util.ArrayList;

import ikshare.protocol.command.Commando;

public class ChatWelcomeCommando extends Commando {
    String nickName;
    ArrayList<String> rooms;
    
    public ChatWelcomeCommando() {
        super();
    }
    
    public ChatWelcomeCommando(String commandoString)
    {
        super(commandoString);
        setNickName(commandoLine.get(1));
    	for(int i = 2; i < commandoLine.size(); i++)
    	{
    		addRoom(commandoLine.get(i));
    	}
    }

    public String getNickName() {
        return nickName;
    }

	public ArrayList<String> getRooms() {
		return rooms;
	}

	public void addRoom(String room) {
		if(rooms==null)
			rooms = new ArrayList<String>();
		rooms.add(room);
	}
	
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        String fullCommando = commandoBundle.getString("chatwelcome")+del+getNickName();
        
        for(String room : rooms)
        {
        	fullCommando+= (del + room);
        }
        
        return fullCommando;
    }
}
