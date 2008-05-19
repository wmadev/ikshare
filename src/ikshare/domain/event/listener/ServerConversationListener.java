package ikshare.domain.event.listener;

import ikshare.protocol.command.Commando;

public interface ServerConversationListener {
    public void receivedCommando(Commando c);
}
