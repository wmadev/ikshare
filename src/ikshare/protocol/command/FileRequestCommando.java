package ikshare.protocol.command;

public class FileRequestCommando extends Commando {

    private String accountName,  fileName,  path, transferId;
    private long sentBytes;
    
    public FileRequestCommando() {
        super();
    }

    public FileRequestCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setFileName(commandoLine.get(2));
        setPath(commandoLine.get(3));
        setTransferId(commandoLine.get(4));
        if (commandoLine.size()==6)
        	setSentBytes(Long.parseLong(commandoLine.get(5)));
    }

    
    
    public long getSentBytes() {
		return sentBytes;
	}

	public void setSentBytes(long sentBytes) {
		this.sentBytes = sentBytes;
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("filerequest")+del+getAccountName()+del+getFileName()+del+getPath()+del+getTransferId()+del+getSentBytes();
    }
}
