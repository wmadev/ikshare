package ikshare.protocol.command;

public class ReceiveFolderIdCommando extends Commando {
    private int folderId;
    
    public ReceiveFolderIdCommando() {
        super();
    }

    public ReceiveFolderIdCommando(String commandoString) {
        super(commandoString);
        setFolderId(Integer.parseInt(commandoLine.get(1)));
    }
    
    public int getFolderId(){
        return folderId;
    }
    public void setFolderId(int f){
        folderId = f;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("receivefolderid")+del+getFolderId();
    }
}
