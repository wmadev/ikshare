package ikshare.protocol.command;

public class PingCommando extends Commando {
    
    public PingCommando() {
        super();
    }

    public PingCommando(String commandoString) {
        super(commandoString);
    // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return commandoBundle.getString("ping");
    }
}
