package ikshare.protocol.command;

public class WelcomeCommando extends Commando {

    private String accountName;
    
    public WelcomeCommando() {
        super();
    }

    public WelcomeCommando(String commandoString) {
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
        return commandoBundle.getString("welcome")+del+getAccountName();
    }
}
