/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.domain;

import java.util.ArrayList;

/**
 *
 * @author awosy
 */
public class SharedItem {
    private boolean folder;
    private ArrayList<SharedItem> sharedItems;
    
    public SharedItem(){
        folder = false;
        sharedItems = new ArrayList<SharedItem>();
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
