package ikshare.chatserver.datatypes;

import ikshare.chatserver.HandleChatClientThread;
import ikshare.protocol.command.*;
import java.net.InetAddress;

public class ChatClient 
{
    private String nickName;
    private InetAddress IP;
    private HandleChatClientThread thread;
    
    public ChatClient() 
    {
    }
    
    public ChatClient(HandleChatClientThread thread) 
    {
    	this.thread = thread;
    }
    
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }
    
    public String getNickName()
    {
        return nickName;
    }
    
    public void setIP(InetAddress ip)
    {
        this.IP = ip;
    }
    
    public InetAddress getIP()
    {
        return IP;
    }
    
	public HandleChatClientThread getThread() {
		return thread;
	}
    
    public void SendMessage(Commando command)
    {
        thread.SendMessage(command);
    }
    

    //@override
    public boolean equals(Object ob)
    {
        return (ob instanceof ChatClient && ((ChatClient)ob).getNickName().equals(this.getNickName()));
    }
}
