package ikshare.protocol.command;

public class FindBasicCommando extends Commando {

    private String searchID;
    private String keyword;

    public FindBasicCommando() {
        super();
    }

    public FindBasicCommando(String commandoString) {
        super(commandoString);
        setSearchID(commandoLine.get(1));
        setKeyword(commandoLine.get(2));
    }

    public String getSearchID() {
        return searchID;
    }

    public void setSearchID(String searchID) {
        this.searchID = searchID;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String k) {
        this.keyword = k;
    }

    @Override
    public String toString() {
        String del = commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("findbasic") + del + getSearchID() + del + getKeyword();
    }
}
