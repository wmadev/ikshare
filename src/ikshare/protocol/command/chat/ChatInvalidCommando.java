package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatInvalidCommando extends Commando
{
	private String message = "";
	
	public ChatInvalidCommando()
	{
		super();
	}
	
	public ChatInvalidCommando(String commandoString)
	{
		super(commandoString);
		if(commandoLine.size()>1)
			setMessage(commandoLine.get(1));
	}

	public String getMessage() 
	{
		return message;
	}

	public void setMessage(String message) 
	{
		this.message = message;
	}
	
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chatinvalid")
        	+ del + getMessage();
    }
}
