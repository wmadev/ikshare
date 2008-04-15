/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.protocol.command;

/**
 *
 * @author awosy
 */
public class StartShareSynchronisationCommando extends Commando {

    private String accountName;

    public StartShareSynchronisationCommando() {
        super();
    }

    public StartShareSynchronisationCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
    }
   
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("startsharesync")+del+getAccountName();
    }
}
