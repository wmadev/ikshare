package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatCreateRoomCommando extends Commando
{
	private String roomName;
	private boolean privateRoom;
	private String password;
	
	public ChatCreateRoomCommando(){
		super();
	}
	
	public ChatCreateRoomCommando(String commandoString)
	{
		super(commandoString);
		setRoomName(commandoLine.get(1));
		setPrivateRoom(commandoLine.get(2).equals("1")?true:false);
		
		if(commandoLine.size()>3)
			setPassword(commandoLine.get(3));
		else
			setPassword("");
	}

	public String getRoomName() 
	{
		return roomName;
	}

	public void setRoomName(String roomName) 
	{
		this.roomName = roomName;
	}
	
	public boolean isPrivateRoom() 
	{
		return privateRoom;
	}

	public void setPrivateRoom(boolean privateRoom) 
	{
		this.privateRoom = privateRoom;
	}
	
	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}
	
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chatcreateroom")
        	+ del + getRoomName()
        	+ del + (isPrivateRoom()?"1":"0")
        	+ del + getPassword();
    }
}
