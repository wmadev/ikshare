/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.protocol.command;

/**
 *
 * @author awosy
 */
public class EndShareSynchronisationCommando extends Commando {

    public EndShareSynchronisationCommando() {
        super();
    }

    public EndShareSynchronisationCommando(String commandoString) {
        super(commandoString);
    }
   
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("endsharesync");
    }
}
