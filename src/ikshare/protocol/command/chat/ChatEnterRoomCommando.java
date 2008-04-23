package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatEnterRoomCommando extends Commando
{
	private String roomName;
	
	public ChatEnterRoomCommando()
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
	
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chatenterroom")
        	+ del + getRoomName();
    }
}
