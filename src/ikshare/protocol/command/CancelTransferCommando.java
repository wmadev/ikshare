package ikshare.protocol.command;

public class CancelTransferCommando extends Commando {

    private String transferId, accountName;

    public CancelTransferCommando() {
        super();
    }

    public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public CancelTransferCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setTransferId(commandoLine.get(2));
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getTransferId() {
        return transferId;
    }

    
    @Override
    public String toString() {
        String del="";
        if (commandoBundle==null) {
            del = "$";
        } else {
            del=commandoBundle.getString("commandoDelimiter");
        }
        return commandoBundle.getString("canceltransfer")+del+getAccountName()+del+getTransferId();
    }
}
