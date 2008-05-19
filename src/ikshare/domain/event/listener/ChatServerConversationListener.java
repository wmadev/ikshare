package ikshare.domain.event.listener;

import ikshare.protocol.command.chat.*;

public interface ChatServerConversationListener {
	public void youLoggedOn(ChatWelcomeCommando c);
    public void receivedMessage(ChatMessageCommando c);
    public void userLeftRoom(ChatHasLeftRoomCommando c);
    public void userEntersRoom(ChatHasEnteredRoomCommando c);
    public void youEnterRoom(ChatYouEnterRoomCommando c);
	public void invalidRoomPassword(ChatInvalidRoomPasswordCommando c);
	public void chatServerInterupt(String message);
	public void chatRoomDoesNotExist(ChatRoomDoesNotExistCommando c);
	public void chatRoomsUpdate(ChatUpdateRoomsListCommando c);
	public void logNiLukNi(ChatLogNiLukNiCommando c);
}
