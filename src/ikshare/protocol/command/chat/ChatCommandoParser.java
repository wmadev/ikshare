package ikshare.protocol.command.chat;

import ikshare.domain.ResourceBundleManager;
import ikshare.protocol.command.Commando;
import ikshare.protocol.exception.*;
import java.util.ResourceBundle;

public class ChatCommandoParser {

    private static ResourceBundle bundle;
    private static ChatCommandoParser instance;

    protected ChatCommandoParser() {
        bundle = ResourceBundleManager.getInstance().getCommandoBundle();
    }
    
    public static ChatCommandoParser getInstance() {
        if (instance==null)
            instance = new ChatCommandoParser();
        return instance;
    }

    public Commando parse(String commandoString) throws CommandNotFoundException {
        if (commandoString.startsWith(bundle.getString("chatmessage"))){
        	return new ChatMessageCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatupdateroomslist"))){
        	return new ChatUpdateRoomsListCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatlogon"))){
        	return new ChatLogOnCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatlogoff"))){
        	return new ChatLogOffCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatwelcome"))){
        	return new ChatWelcomeCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatlognilukni"))){
        	return new ChatLogNiLukNiCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatenterroom"))){
        	return new ChatEnterRoomCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatleaveroom"))){
        	return new ChatLeaveRoomCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatyouenterroom"))){
        	return new ChatYouEnterRoomCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chathasenteredroom"))){
        	return new ChatHasEnteredRoomCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chathasleftroom"))){
        	return new ChatHasLeftRoomCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatcreateroom"))){
        	return new ChatCreateRoomCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatinvalidroompassword"))){
        	return new ChatInvalidRoomPasswordCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatroomdoesnotexist"))){
        	return new ChatRoomDoesNotExistCommando(commandoString);
        }
        else if (commandoString.startsWith(bundle.getString("chatinvalidcommand"))){
        	return new ChatInvalidCommando(commandoString);
        }
        else{
        	throw new CommandNotFoundException(commandoString);
        }
    }
}
