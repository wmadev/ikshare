package ikshare.protocol.command;

public class FindAdvancedFolderCommando extends Commando {
    private String searchID, keyword;
    private long minSize, maxSize;
    private boolean textAnd;

    public FindAdvancedFolderCommando() {
        super();
    }

    public FindAdvancedFolderCommando(String commandoString) {
        super(commandoString);
        setSearchID(commandoLine.get(1));
        setKeyword(commandoLine.get(2));
        setTextAnd(Boolean.parseBoolean(commandoLine.get(3)));
        setMinSize(Long.parseLong(commandoLine.get(4)));
        setMaxSize(Long.parseLong(commandoLine.get(5)));
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

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public long getMinSize() {
        return minSize;
    }

    public void setMinSize(long minSize) {
        this.minSize = minSize;
    }

    public boolean isTextAnd() {
        return textAnd;
    }

    public void setTextAnd(boolean textAnd) {
        this.textAnd = textAnd;
    }
       
    @Override
    public String toString() {
        String del = commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("findadvancedfolder") + del + getSearchID() + del + getKeyword() +
                del + isTextAnd() + del + getMinSize() + del + getMaxSize();
    }
}
