package ikshare.protocol.command;

import java.util.HashMap;
import java.util.Map;

public class FoundCommando extends Commando {

    private String searchID,fileName,path;
    private HashMap<String, String> metaData;

    public FoundCommando(String commandoString) {
        super(commandoString);

        setSearchID(commandoLine.get(1));
        setFileName(commandoLine.get(2));
        setPath(commandoLine.get(3));
        if (commandoLine.size() > 4) {
            for (int i = 4; i < commandoLine.size(); i++) {
                String[] couple = commandoLine.get(4).split("=");
                metaData.put(couple[0], couple[1]);
            }
        }

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public HashMap<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(HashMap<String, String> metaData) {
        this.metaData = metaData;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        String output = commandoBundle.getString("found")+del+getSearchID()+del+getFileName()+del+getPath();
        
        for(Map.Entry<String, String> entry:metaData.entrySet()) {
            output+=del+entry.getKey()+"="+entry.getValue();
        }
        
        return output;
    }
    
}
