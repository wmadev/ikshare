package ikshare.protocol.command;

public class LogOffCommando extends Commando {

    private String accountName,  password;
    
    public LogOffCommando() {
        super();
    }

    public LogOffCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setPassword(commandoLine.get(2));
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("logoff")+del+getAccountName()+del+getPassword();
    }
    
}
