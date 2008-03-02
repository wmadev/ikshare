package ikshare.protocol.command;

public class FindCommando extends Commando {
    private String searchID;
    private String searchQuery;

	public FindCommando(String commandoString) {
		super(commandoString);
		// TODO Auto-generated constructor stub
                setSearchID(commandoLine.get(1));
                setSearchQuery(commandoLine.get(2));
	}

    public String getSearchID() {
        return searchID;
    }

    public void setSearchID(String searchID) {
        this.searchID = searchID;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
        
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("find")+del+getSearchID()+del+getSearchQuery();
    }
}
