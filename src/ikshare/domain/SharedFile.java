package ikshare.domain;


public class SharedFile extends SharedItem {
	private static final long serialVersionUID = 1L;
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
    @Override
    public boolean equals(Object o){
        SharedFile f = (SharedFile)o;
        if(f.getFolderID() == this.getFolderID() && f.getName().equals(this.getName()) && f.getSize() == this.getSize()){
            return true;
        }
        return false;
    }
    
    
    
}
