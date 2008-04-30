package ikshare.protocol.command;

public class WelcomeCommando extends Commando {

    private String accountName;
    private String ipAddress;
    
    public WelcomeCommando() {
        super();
    }

    public WelcomeCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setIpAddress(commandoLine.get(2));
    }

    public void setIpAddress(String ip){
        this.ipAddress = ip;
    }
    
    public String getIpAddress(){
        return ipAddress;
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
        return commandoBundle.getString("welcome")+del+getAccountName()+del+getIpAddress();
    }
}
