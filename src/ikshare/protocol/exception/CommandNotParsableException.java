package ikshare.protocol.exception;

public class CommandNotParsableException extends Exception {
	public CommandNotParsableException(String message) {
		super(message + " is not parsable.");
	}

}
