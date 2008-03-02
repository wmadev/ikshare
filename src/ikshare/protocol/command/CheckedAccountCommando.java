package ikshare.protocol.command;

public class CheckedAccountCommando extends Commando {

    private String accountName, status;

    public CheckedAccountCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setStatus(commandoLine.get(2));
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return commandoBundle.getString("checkedaccount")+commandoBundle.getString("commandoDelimiter")+getAccountName()+commandoBundle.getString("commandoDelimiter")+getStatus();
    }
    
}
