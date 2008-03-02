package ikshare.protocol.command;

public class FoundItAllCommando extends Commando {

    private String searchID;

    public FoundItAllCommando(String commandoString) {
        super(commandoString);
        setSearchID(commandoLine.get(2));
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
        return commandoBundle.getString("founditall")+del+getSearchID();
    }
}
