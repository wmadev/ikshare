package ikshare.domain.event.listener;

import ikshare.protocol.command.chat.ChatHasEnteredRoomCommando;
import ikshare.protocol.command.chat.ChatHasLeftRoomCommando;
import ikshare.protocol.command.chat.ChatInvalidRoomPasswordCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;
import ikshare.protocol.command.chat.ChatWelcomeCommando;
import ikshare.protocol.command.chat.ChatYouEnterRoomCommando;

/**
 *
 * @author Boris
 */
public interface ChatServerConversationListener {
	public void youLoggedOn(ChatWelcomeCommando c);
    public void receivedMessage(ChatMessageCommando c);
    public void userLeftRoom(ChatHasLeftRoomCommando c);
    public void userEntersRoom(ChatHasEnteredRoomCommando c);
    public void youEnterRoom(ChatYouEnterRoomCommando c);
	public void invalidRoomPassword(ChatInvalidRoomPasswordCommando c);
	public void chatServerInterupt(String message);
}
