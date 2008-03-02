package ikshare.protocol.command;

public class LogNiLukNiCommando extends Commando {

    private String accountName, message;
    
    public LogNiLukNiCommando() {
        super();
    }

    public LogNiLukNiCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setMessage(commandoLine.get(2));
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("luknilukni")+del+getAccountName()+del+getMessage();
    }
}
