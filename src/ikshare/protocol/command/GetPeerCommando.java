package ikshare.protocol.command;

public class GetPeerCommando extends Commando {

    private String accountName;
    
    public GetPeerCommando() {
        super();
    }

    public GetPeerCommando(String commandoString) {
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
        return commandoBundle.getString("peer")+del+getAccountName();
    }
}
