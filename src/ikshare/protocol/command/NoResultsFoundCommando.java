package ikshare.protocol.command;

public class NoResultsFoundCommando extends Commando {
    private String searchID;
    private String keyword;
    
    public NoResultsFoundCommando() {
        super();
    }
    
    public NoResultsFoundCommando(String commandoString) {
        super(commandoString);
        setSearchID(commandoLine.get(1));
        setKeyword(commandoLine.get(2));
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
        return commandoBundle.getString("noresultsfound")+del+getSearchID()+del+getKeyword();
    }
    
}
