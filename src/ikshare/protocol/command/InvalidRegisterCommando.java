package ikshare.protocol.command;

public class InvalidRegisterCommando extends Commando {

    private String message;
    
    public InvalidRegisterCommando() {
        super();
    }

    public InvalidRegisterCommando(String commandoString) {
        super(commandoString);
        setMessage(commandoLine.get(1));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String accountName) {
        this.message = accountName;
    }
    
    @Override
    public String toString() {
        String del=commandoBundle.getString("commandoDelimiter");
        return commandoBundle.getString("invalidregister")+del+getMessage();
    }
}
