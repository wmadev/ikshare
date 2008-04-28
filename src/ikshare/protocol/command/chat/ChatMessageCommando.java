package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

/**
 * @author Boris
 *
 */
public class ChatMessageCommando extends Commando
{
	private String text;
    private String recipient;
    private String sender;
    private boolean privateMessage = false;
    
    public ChatMessageCommando()
    {
    	super();
    }
    
    public ChatMessageCommando(String commandoString)
    {
    	super(commandoString);
    	setSender(commandoLine.get(1));
    	setRecipient(commandoLine.get(2));
    	if(commandoLine.get(3).equals(1))
    		setPrivateMessage(true);
    	else
    		setPrivateMessage(false);
    	setText(commandoLine.get(4));
    }

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public boolean isPrivateMessage() {
		return privateMessage;
	}

	public void setPrivateMessage(boolean privateMessage) {
		this.privateMessage = privateMessage;
	}
	
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        char privmsg = isPrivateMessage()?'1':'0';
        return commandoBundle.getString("chatmessage")
        	+ del + getSender() 
        	+ del + getRecipient()
        	+ del + privmsg
        	+ del + getText();
    }
}
