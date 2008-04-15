/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ikshare.protocol.command;

/**
 *
 * @author awosy
 */
public class AddShareCommando extends Commando {
    private boolean directory;
    private String path;
    private String name;
    private int parentFolderID;
    private long size;
    
    public AddShareCommando() {
        super();
    }

    public AddShareCommando(String commandoString) {
        super(commandoString);
        if(commandoLine.get(1).equals("D")){
            setDirectory(true);
            setPath(commandoLine.get(4));
        }
        else{
            setDirectory(false);
            setSize(Long.parseLong(commandoLine.get(4)));
        }
        setParentFolderID(Integer.parseInt(commandoLine.get(2)));
        setName(commandoLine.get(3));
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        return commandoBundle.getString("addshare")+del+(isDirectory()?"D":"F")+del+getParentFolderID()+del+getName()+del+(isDirectory()?getPath():getSize());
    }

}