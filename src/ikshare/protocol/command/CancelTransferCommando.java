package ikshare.protocol.command;

public class CancelTransferCommando extends Commando {

    private String transferId;

    public CancelTransferCommando() {
        super();
    }

    public CancelTransferCommando(String commandoString) {
        super(commandoString);
        setTransferId(commandoLine.get(1));
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
        return commandoBundle.getString("canceltransfer")+del+getTransferId();
    }
}
