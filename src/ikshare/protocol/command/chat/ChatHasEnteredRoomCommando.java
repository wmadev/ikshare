package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatHasEnteredRoomCommando extends Commando
{
	private String roomName;
	private String nickName;
	
	public ChatHasEnteredRoomCommando(){
		super();
	}
	
	public ChatHasEnteredRoomCommando(String commandoString)
	{
		super(commandoString);
		setNickName(commandoLine.get(1));
		setRoomName(commandoLine.get(2));
	}

	public String getRoomName() 
	{
		return roomName;
	}

	public void setRoomName(String roomName) 
	{
		this.roomName = roomName;
	}
	
	public String getNickName() 
	{
		return nickName;
	}

	public void setNickName(String nickName) 
	{
		this.nickName = nickName;
	}
	
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chathasenteredroom")
        	+ del + getNickName()
        	+ del + getRoomName();
    }
}
