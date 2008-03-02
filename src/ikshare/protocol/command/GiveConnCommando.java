package ikshare.protocol.command;

public class GiveConnCommando extends Commando {

    private int port;

    public GiveConnCommando(String commandoString) {
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
        return commandoBundle.getString("conn")+del+getPort();
    }
}
