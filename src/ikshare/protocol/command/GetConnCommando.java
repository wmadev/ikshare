package ikshare.protocol.command;

public class GetConnCommando extends Commando {

    private int port;
    
    public GetConnCommando() {
        super();
    }
    
    public GetConnCommando(String commandoString) {
        super(commandoString);
        setPort(Integer.parseInt(commandoLine.get(1)));
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("getconn")+del+getPort();
    }
}
