package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatLogOnCommando extends Commando {
    String nickName;
    int port;
    
    public ChatLogOnCommando() {
        super();
    }
    
    public ChatLogOnCommando(String commandoString)
    {
        super(commandoString);
        setNickName(commandoLine.get(1));
        setPort(Integer.parseInt(commandoLine.get(2)));
    }
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chatlogon")+del+getNickName()+del+getPort();
    }
}
