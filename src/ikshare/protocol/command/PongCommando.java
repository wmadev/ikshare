package ikshare.protocol.command;

import ikshare.protocol.exception.NotAvailableException;

public class PongCommando extends Commando {

    private String accountName;
    
    public PongCommando() {
        super();
    }

    public PongCommando(String commandoString) {
        super(commandoString);
        // TODO Auto-generated constructor stub
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
        String del = commandoBundle.getString("commandoDelimiter");
			return commandoBundle.getString("pong") + del + getAccountName();
    }
}
