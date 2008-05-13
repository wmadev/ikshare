package ikshare.domain;

import java.util.HashMap;
import java.util.Map;

public class SharedFolder extends SharedItem {

	private static final long serialVersionUID = 1L;
	private String accountName;
    private String path;
    private int parentID;
    private int folderID;
    
    private Map<String,SharedItem> content = new HashMap<String,SharedItem>();

    public SharedFolder(boolean b, String path) {
        super.setFolder(b);
        setPath(path);
        setParentID(0);
    }

    public SharedFolder(boolean folder,String accountName,String path){
        super.setFolder(folder);
        setAccountName(accountName);
        setPath(path);
    }

    public Map<String,SharedItem> getContent(){
        return content;
    }
    
    public void setContent(Map<String,SharedItem> c){
        content = c;
    }
    
    public String getName(){
        return path;//.substring(path.lastIndexOf(System.getProperty("file.separator"))+1);
    }
    
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }
    
    public String getPath() {
        if(getParentId()!=0){
            return path.substring(path.lastIndexOf(System.getProperty("file.separator")));
        }
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    @Override
    public boolean equals(Object o){
        return true;
    }
    
    
}
