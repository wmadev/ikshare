package ikshare.protocol.command;

public class PauseTransferCommando extends Commando {

    private String transferId, accountName;
    private long sentBlocks;
    
    
    public long getSentBlocks() {
		return sentBlocks;
	}

	public void setSentBlocks(long sentBlocks) {
		this.sentBlocks = sentBlocks;
	}

	public PauseTransferCommando() {
        super();
    }

    public PauseTransferCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setTransferId(commandoLine.get(2));
        setSentBlocks(Long.parseLong(commandoLine.get(3)));
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
        return commandoBundle.getString("pausetransfer")+del+getAccountName()+del+getTransferId()+del+getSentBlocks();
    }

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
