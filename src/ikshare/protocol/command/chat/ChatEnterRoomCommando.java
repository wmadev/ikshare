package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatEnterRoomCommando extends Commando
{
	private String roomName;
	private String password;
	private boolean privateRoom;
	
	public ChatEnterRoomCommando(){
		super();
	}
	
	public ChatEnterRoomCommando(String commandoString)
	{
		super(commandoString);
		setRoomName(commandoLine.get(1));
		
		if(commandoLine.get(2).equals("0"))
			setPrivateRoom(false);
		else
			setPrivateRoom(true);
		
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
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPrivateRoom() {
		return privateRoom;
	}

	public void setPrivateRoom(boolean privateRoom) {
		this.privateRoom = privateRoom;
	}
	
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chatenterroom")
        	+ del + getRoomName()
        	+ del + (isPrivateRoom()?1:0)
        	+ del + getPassword();
    }
}
