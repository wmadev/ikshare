package ikshare.protocol.command;

public class CreatedAccountCommando extends Commando {

    private String accountName;
    
    public CreatedAccountCommando() {
        super();
    }

    public CreatedAccountCommando(String commandoString) {
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
        return commandoBundle.getString("createdaccount")+commandoBundle.getString("commandoDelimiter")+getAccountName();
    }
}
