package ikshare.protocol.command;

import java.util.HashMap;
import java.util.Map;

public class FoundResultCommando extends Commando {

    private String searchID;
    private String name;
    private String accountName;
    private boolean folder;
    private long size;
    
    public FoundResultCommando() {
        super();
    }
    
    public FoundResultCommando(String commandoString) {
        super(commandoString);
        setSearchID(commandoLine.get(1));
        if(commandoLine.get(2).equals("D")){
            setFolder(true);
        }else{
            setFolder(false);
        }
        setName(commandoLine.get(3));
        setAccountName(commandoLine.get(4));
        setSize(Long.parseLong(commandoLine.get(5)));
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    

    public String getSearchID() {
        return searchID;
    }

    public void setSearchID(String searchID) {
        this.searchID = searchID;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("foundresult")+del+getSearchID()+del+(isFolder()?"D":"F")+
                del+getName()+
                del+getAccountName()+del+getSize();
    }
    
}
