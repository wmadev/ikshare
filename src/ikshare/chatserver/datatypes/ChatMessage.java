package ikshare.chatserver.datatypes;

public class ChatMessage 
{
    private String text;
    private String recipient;
    private String sender;
    private boolean privateMessage = false;
    
    public ChatMessage() 
    {
    }
    
    public void setText(String text)
    {
        this.text = text;
    }
    
    public String getText()
    {
        return text;
    }
    
    public void setRecipient(String recipient)
    {
        this.recipient = recipient;
    }
    
    public String getRecipient()
    {
        return recipient;
    }

    public void setSender(String sender) 
    {
        this.sender = sender;
    }

    public String getSender() 
    {
        return sender;
    }
    
    public void setPrivateMessage(boolean priv)
    {
        this.privateMessage = priv;
    }
    
    public boolean isPrivateMessage()
    {
        return privateMessage;
    }
}
