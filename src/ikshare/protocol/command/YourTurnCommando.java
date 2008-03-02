package ikshare.protocol.command;

public class YourTurnCommando extends Commando {

    private String accountName,  fileName,  path;
    private int blocks,  blockSize;

    public YourTurnCommando() {
        super();
    }
    
    public YourTurnCommando(String commandoString) {
        super(commandoString);
        setAccountName(commandoLine.get(1));
        setFileName(commandoLine.get(2));
        setPath(commandoLine.get(3));
        setBlocks(Integer.parseInt(commandoLine.get(4)));
        if (commandoLine.size()==6)
            setBlockSize(Integer.parseInt(commandoLine.get(5)));
        else
            setBlockSize(Integer.parseInt(commandoBundle.getString("blocksize")));
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("yourturn")+del+getAccountName()+del+getFileName()+del+getPath()+del+getBlocks()+del+getBlockSize();
    }  
}
