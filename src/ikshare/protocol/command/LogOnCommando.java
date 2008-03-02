package ikshare.protocol.command;

public class LogOnCommando extends Commando {

    private String accountName,  password;
    
    public LogOnCommando() {
        super();
    }

    public LogOnCommando(String commandoString) {
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
        return commandoBundle.getString("logon")+del+getAccountName()+del+getPassword();
    }
    
}
