package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatRoomDoesNotExistCommando extends Commando
{
	private String roomName;
	
	public ChatRoomDoesNotExistCommando(){
		super();
	}
	
	public ChatRoomDoesNotExistCommando(String commandoString)
	{
		super(commandoString);
		setRoomName(commandoLine.get(1));
	}

	public String getRoomName() 
	{
		return roomName;
	}

	public void setRoomName(String roomName) 
	{
		this.roomName = roomName;
	}
	
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chatroomdoesnotexist")
        	+ del + getRoomName();
    }
}
