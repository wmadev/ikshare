package ikshare.protocol.command;

public class DownloadInformationRequestCommand extends Commando{

    private String accountName;
    private String filename;
    private long filesize;
    private int folderid;
    
    public DownloadInformationRequestCommand() {
        super();
    }

    public DownloadInformationRequestCommand(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setFileName(commandoLine.get(2));
        setFileSize(Long.parseLong(commandoLine.get(3)));
        setFolderId(Integer.parseInt(commandoLine.get(4)));
    }
    
    public String getFileName() {
        return filename;
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public long getFileSize() {
        return filesize;
    }

    public void setFileSize(long filesize) {
        this.filesize = filesize;
    }

    public int getFolderId() {
        return folderid;
    }

    public void setFolderId(int folderid) {
        this.folderid = folderid;
    }

    

    public String getAccountName() {
    	return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public String toString() {
        String del = commandoBundle.getString("commandoDelimiter");
	return commandoBundle.getString("requestdownloadinformation") + del + getAccountName()
                +del+getFileName()+del+getFileSize()+del+getFolderId();
    }

}
