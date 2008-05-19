package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

public class ChatLogNiLukNiCommando extends Commando {
    String nickName, message="";
    
    public ChatLogNiLukNiCommando() {
        super();
    }
    
    public ChatLogNiLukNiCommando(String commandoString)
    {
        super(commandoString);
        setNickName(commandoLine.get(1));
        setMessage(commandoLine.get(2));
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("chatlognilukni")+del+getNickName()+del+getMessage();
    }
}
