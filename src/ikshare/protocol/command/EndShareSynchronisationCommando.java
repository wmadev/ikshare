package ikshare.protocol.command;

public class EndShareSynchronisationCommando extends Commando {

    public EndShareSynchronisationCommando() {
        super();
    }

    public EndShareSynchronisationCommando(String commandoString) {
        super(commandoString);
    }
   
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("endsharesync");
    }
}
