package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatEnterRoomCommando extends Commando
{
	private String roomName;
	private String password;
	
	public ChatEnterRoomCommando(){
		super();
	}
	
	public ChatEnterRoomCommando(String commandoString)
	{
		super(commandoString);
		setRoomName(commandoLine.get(1));
		
		if(commandoLine.size()>2)
			setPassword(commandoLine.get(2));
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
	
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chatenterroom")
        	+ del + getRoomName()
        	+ del + getPassword();
    }
}
