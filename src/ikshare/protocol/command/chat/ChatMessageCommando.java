package ikshare.protocol.command.chat;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
    
    private ArrayList<String> arguments;
    
    public ChatMessageCommando()
    {
    	super();
    }
    
    public ChatMessageCommando(String commandoString)
    {
        super(commandoString);
        
        arguments = new ArrayList<String>();
        
        StringTokenizer commandTokenizer = new StringTokenizer(commandoString, commandoBundle.getString("commandoDelimiter"), true);
        
        while(commandTokenizer.hasMoreTokens() && arguments.size() < 4)
        {
            String nextToken = commandTokenizer.nextToken();
            if(nextToken.equals(commandoBundle.getString("commandoDelimiter")))
            {
                arguments.add("");
            }
            else
            {
                arguments.add(nextToken);
                if(commandTokenizer.hasMoreTokens())
                    commandTokenizer.nextToken();
            }
        }

        text = "";
        
        while(commandTokenizer.hasMoreTokens())
        {
            text += commandTokenizer.nextToken();
        }

    	setSender(arguments.get(1));
    	setRecipient(arguments.get(2));
    	if(arguments.get(3).equals(1))
    		setPrivateMessage(true);
    	else
    		setPrivateMessage(false);
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
