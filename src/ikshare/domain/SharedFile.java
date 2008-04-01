package ikshare.domain;

/**
 *
 * @author awosy
 */
public class SharedFile extends SharedItem {
    private String name;
    private int folderID;
    private long size;

    public SharedFile(boolean folder,String name,long size){
        super.setFolder(folder);
        setName(name);
        setSize(size);
    }
    
    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
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
    
    
    
}
