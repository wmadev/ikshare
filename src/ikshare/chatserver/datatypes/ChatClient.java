package ikshare.chatserver.datatypes;

import java.net.InetAddress;

/**
 *
 * @author Boris martens
 */
public class ChatClient 
{
    private String nickName;
    private InetAddress IP;
    private int port;
    
    public ChatClient() 
    {
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
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public void SendMessage(ChatMessage message)
    {
        
    }
    
    //@override
    public boolean equals(Object ob)
    {
        return (ob instanceof ChatClient && ((ChatClient)ob).getNickName().equals(this.getNickName()));
    }
}
