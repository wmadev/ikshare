package ikshare.protocol.command.chat;

import ikshare.protocol.command.Commando;

/**
 *
 * @author Boris martens
 */
public class ChatWelcomeCommando extends Commando {
    String nickName;
    
    public ChatWelcomeCommando() {
        super();
    }
    
    public ChatWelcomeCommando(String commandoString)
    {
        super(commandoString);
        setNickName(commandoLine.get(1));
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
        return commandoBundle.getString("chatwelcome")+del+getNickName();
    }
}
