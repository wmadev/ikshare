package ikshare.protocol.command;

public class GoForItCommando extends Commando {

    private String accountName, transferId;
    
    public GoForItCommando() {
        super();
    }

    public GoForItCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setTransferId(commandoLine.get(2));
    }

    
    public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }
   
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("goforit")+del+getAccountName()+del+getTransferId();
    }
}
