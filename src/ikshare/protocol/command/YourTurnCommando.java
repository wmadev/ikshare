package ikshare.protocol.command;

public class YourTurnCommando extends Commando {

    private String accountName,  fileName,  path, transferId;
    private long size,  blockSize;

    public YourTurnCommando() {
        super();
    }
    
    public YourTurnCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setFileName(commandoLine.get(2));
        setPath(commandoLine.get(3));
        setTransferId(commandoLine.get(4));
        setSize(Integer.parseInt(commandoLine.get(5)));
        if (commandoLine.size()==7)
            setBlockSize(Integer.parseInt(commandoLine.get(6)));
        else
            setBlockSize(Integer.parseInt(commandoBundle.getString("blocksize")));
    }

    
    
    public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public long getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(long blockSize) {
        this.blockSize = blockSize;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("yourturn")+del+getAccountName()+del+getFileName()+del+getPath()+del+getTransferId()+del+getSize()+del+getBlockSize();
    }  
}
