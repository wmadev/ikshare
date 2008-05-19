package ikshare.protocol.command;

public class ServerErrorCommando extends Commando {
    private String message;
    
    public ServerErrorCommando(){
        super();
    }
    
    public ServerErrorCommando(String commandoString){
        super(commandoString);
        setMessage(commandoLine.get(1));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        String del = commandoBundle.getString("commandoDelimiter");
	return commandoBundle.getString("servererror") + del + getMessage();
    }

}
