/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;

/**
 *
 * @author awosy
 */
public class SharedFolder extends SharedItem {
    private String accountName;
    private String path;
    private int parentID;
    private int folderID;

    public SharedFolder(boolean folder,String accountName,String path){
        super.setFolder(folder);
        setAccountName(accountName);
        setPath(path);
    }

    public String getName(){
        return path.substring(path.lastIndexOf(System.getProperty("file.separator"))+1);
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
