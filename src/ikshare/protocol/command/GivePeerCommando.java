package ikshare.protocol.command;

public class GivePeerCommando extends Commando {

    private String accountName,  ipAddress;
    private int port;

    public GivePeerCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setIpAddress(commandoLine.get(2));
        setPort(Integer.parseInt(commandoLine.get(3)));
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("givepeer")+del+getAccountName()+del+getIpAddress()+del+getPort();
    }
}
