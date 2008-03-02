package ikshare.protocol.exception;

public class CommandNotFoundException extends Exception {
	public CommandNotFoundException(String message) {
		super(message + " is not a command.");
	}

}
