package ikshare.protocol.command;

public class FindAdvancedFileCommando extends Commando {
    private String searchID, keyword;
    private int typeID;
    private long minSize, maxSize;
    private boolean textAnd;

    public FindAdvancedFileCommando() {
        super();
    }

    public FindAdvancedFileCommando(String commandoString) {
        super(commandoString);
        setSearchID(commandoLine.get(1));
        setKeyword(commandoLine.get(2));
        setTextAnd(Boolean.parseBoolean(commandoLine.get(3)));
        setTypeID(Integer.parseInt(commandoLine.get(4)));
        setMinSize(Long.parseLong(commandoLine.get(5)));
        setMaxSize(Long.parseLong(commandoLine.get(6)));
        
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

    public boolean isTextAnd() {
        return textAnd;
    }

    public void setTextAnd(boolean textAnd) {
        this.textAnd = textAnd;
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

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }
    
    @Override
    public String toString() {
        String del = commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("findadvancedfile") + del + getSearchID() + del + getKeyword() + 
                del + isTextAnd() + del + getTypeID() + del + getMinSize() + del + getMaxSize();
    }
}
