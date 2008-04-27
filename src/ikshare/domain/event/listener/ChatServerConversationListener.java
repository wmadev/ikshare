package ikshare.domain.event.listener;

import ikshare.protocol.command.chat.ChatHasEnteredRoomCommando;
import ikshare.protocol.command.chat.ChatHasLeftRoomCommando;
import ikshare.protocol.command.chat.ChatMessageCommando;
import ikshare.protocol.command.chat.ChatYouEnterRoomCommando;

/**
 *
 * @author Boris
 */
public interface ChatServerConversationListener {
    public void receivedMessage(ChatMessageCommando c);
    public void userLeftRoom(ChatHasLeftRoomCommando c);
    public void userEntersRoom(ChatHasEnteredRoomCommando c);
    public void youEnterRoom(ChatYouEnterRoomCommando c);
}
