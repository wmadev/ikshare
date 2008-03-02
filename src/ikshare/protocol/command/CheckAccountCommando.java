package ikshare.protocol.command;

public class CheckAccountCommando extends Commando {

    private String accountName;
    
    public CheckAccountCommando() {
        super();
    }

    public CheckAccountCommando(String commandoString) {
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
        return commandoBundle.getString("checkaccount")+commandoBundle.getString("commandoDelimiter")+getAccountName();
    }
}
