package ikshare.protocol.command;

public class NeverMindCommando extends Commando {

    private String searchID;

    public NeverMindCommando(String commandoString) {
        super(commandoString);
        setSearchID(commandoLine.get(1));
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
        return commandoBundle.getString("nevermind")+del+getSearchID();
    }
}
