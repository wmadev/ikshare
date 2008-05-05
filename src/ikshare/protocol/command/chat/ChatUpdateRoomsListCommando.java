package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatUpdateRoomsListCommando extends Commando
{
	private String roomName;
	private boolean added; //true if room is added, false if it is removed
	
	public ChatUpdateRoomsListCommando(){
		super();
	}
	
	public ChatUpdateRoomsListCommando(String commandoString)
	{
		super(commandoString);
		setRoomName(commandoLine.get(1));
		setAdded(commandoLine.get(2).equals("1")?true:false);
	}

	public String getRoomName() 
	{
		return roomName;
	}

	public void setRoomName(String roomName) 
	{
		this.roomName = roomName;
	}
	
	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}
	
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chatupdateroomslist")
        	+ del + getRoomName()
        	+ del + (isAdded()?"1":"0");
    }


}
