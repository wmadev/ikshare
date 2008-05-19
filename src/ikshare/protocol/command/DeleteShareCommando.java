package ikshare.protocol.command;

public class DeleteShareCommando extends Commando {
    private boolean directory;
    private int folderId;
    private String name;
    private int parentFolderID;
    private long size;
    
    public DeleteShareCommando() {
        super();
    }

    public DeleteShareCommando(String commandoString) {
        super(commandoString);
        if(commandoLine.get(1).equals("D")){
            setDirectory(true);
            setFolderId(Integer.parseInt(commandoLine.get(2)));
        }
        else{
            setDirectory(false);
            setParentFolderID(Integer.parseInt(commandoLine.get(2)));
            setName(commandoLine.get(3));
            setSize(Long.parseLong(commandoLine.get(4)));
        }
        
        
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentFolderID() {
        return parentFolderID;
    }

    public void setParentFolderID(int parentFolderID) {
        this.parentFolderID = parentFolderID;
    }

    public void setFolderId(int id){
        folderId = id;
    }
    
    public int getFolderId(){
        return folderId;
    }
    
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    
   
    
   
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        if(isDirectory()){
            return commandoBundle.getString("deleteshare")+del+"D"+del+getFolderId();
        }
        else{
            return commandoBundle.getString("deleteshare")+del+"F"+del+getParentFolderID()+del+getName()+del+getSize();
        }
    }
}
