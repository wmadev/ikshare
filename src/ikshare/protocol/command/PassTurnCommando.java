package ikshare.protocol.command;

public class PassTurnCommando extends Commando {

    private String accountName,  fileName,  path;

    public PassTurnCommando(String commandoString) {
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
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("passturn")+del+getAccountName()+del+getFileName()+del+getPath();
    }
}
