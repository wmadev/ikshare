package ikshare.protocol.command;

import java.util.HashMap;
import java.util.Map;

public class FoundResultCommando extends Commando {

    private String searchID;
    private int folderid;
    private int parentid;
    private String name;
    private String accountName;
    private boolean folder;
    private long size;
    private String searchKeyword;
    
    public FoundResultCommando() {
        super();
    }
    
    public FoundResultCommando(String commandoString) {
        super(commandoString);
        setSearchID(commandoLine.get(1));
        setSearchKeyword(commandoLine.get(2));
        if(commandoLine.get(3).equals("D")){
            setFolder(true);
        }else{
            setFolder(false);
        }
        setName(commandoLine.get(4));
        setAccountName(commandoLine.get(5));
        setSize(Long.parseLong(commandoLine.get(6)));
        setParentId(Integer.parseInt(commandoLine.get(7)));
        setFolderId(Integer.parseInt(commandoLine.get(8)));
    }

    public void setFolderId(int id){
        this.folderid = id;
    }
    public int getFolderId(){
        return folderid;
    }
    public int getParentId() {
        return parentid;
    }

    public void setParentId(int parentid) {
        this.parentid = parentid;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
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
        return commandoBundle.getString("foundresult")+del+getSearchID()+del+getSearchKeyword()+del+(isFolder()?"D":"F")+
                del+getName()+
                del+getAccountName()+del+getSize()+del+getParentId()+del+getFolderId();
    }
    
}
