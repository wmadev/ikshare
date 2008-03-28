/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain.event.listener;

import ikshare.protocol.command.Commando;

/**
 *
 * @author awosy
 */
public interface ServerConversationListener {
    public void receivedCommando(Commando c);
}
