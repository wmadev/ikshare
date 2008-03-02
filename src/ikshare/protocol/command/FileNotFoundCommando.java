package ikshare.protocol.command;

public class FileNotFoundCommando extends Commando {

    private String accountName,  fileName,  path;

    public FileNotFoundCommando() {
        super();
    }

    public FileNotFoundCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setFileName(commandoLine.get(2));
        setPath(commandoLine.get(3));
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
        String del="";
        if (commandoBundle==null) {
            del = "$";
        } else {
            del=commandoBundle.getString("commandoDelimiter");
        }
        return commandoBundle.getString("filenotfound")+del+getAccountName()+del+getFileName()+del+getPath();
    }
}
