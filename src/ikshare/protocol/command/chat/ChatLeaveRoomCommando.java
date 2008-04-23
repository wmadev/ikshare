package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatLeaveRoomCommando extends Commando
{
	private String roomName;
	
	public ChatLeaveRoomCommando()
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
        return commandoBundle.getString("chatleaveroom")
        	+ del + getRoomName();
    }
}
