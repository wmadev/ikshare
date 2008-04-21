package ikshare.domain;

import java.io.File;
import java.util.HashMap;

public class SearchResult {

    private String owner;
    private String name;
    private long size;
    private boolean folder;
    private int parentid;

    public SearchResult(String name, String owner, long size, boolean folder) {
        this.name = name;
        this.owner = owner;
        this.size = size;
        this.folder = folder;
    }

    public SearchResult(String id,String name, long size, String accountName, boolean folder, int parentid) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.owner = accountName;
        this.folder = folder;
        this.parentid = parentid;
    }
    
     
    public SearchResult(String name, String accountName, long size, boolean folder, int parentid) {
        this.name = name;
        this.size = size;
        this.owner = accountName;
        this.folder = folder;
        this.parentid = parentid;
    }

    public SearchResult(String searchID, String name, long size, String accountName, boolean folder) {
        this.id = searchID;
        this.name = name;
        this.size = size;
        this.owner = accountName;
        this.folder = folder;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getParentId() {
        return parentid;
    }

    public void setParentId(int id) {
        parentid = id;
    }
    private Peer peer;
    private IKShareFile file;
    private HashMap<String, String> metadata;
    private String id;

    public SearchResult(String id, Peer peer, IKShareFile file) {
        this.id = id;
        this.peer = peer;
        this.file = file;
        metadata = new HashMap<String, String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IKShareFile getFile() {
        return file;
    }

    public void setFile(IKShareFile file) {
        this.file = file;
    }

    public HashMap<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(HashMap<String, String> metadata) {
        this.metadata = metadata;
    }

    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }
}
