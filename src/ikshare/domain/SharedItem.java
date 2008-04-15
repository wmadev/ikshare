/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author awosy
 */
public class SharedItem implements Serializable{
    private boolean folder;
    private ArrayList<SharedItem> sharedItems;
    private SharedItemState state;
    private int parentId;
        
    public SharedItem(){
        folder = false;
        sharedItems = new ArrayList<SharedItem>();
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    
    
    public SharedItemState getState() {
        return state;
    }

    public void setState(SharedItemState state) {
        this.state = state;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public ArrayList<SharedItem> getSharedItems() {
        return sharedItems;
    }

    public void setSharedItems(ArrayList<SharedItem> sharedItems) {
        this.sharedItems = sharedItems;
    }

}
