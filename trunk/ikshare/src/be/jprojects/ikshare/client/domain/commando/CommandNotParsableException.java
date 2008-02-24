package be.jprojects.ikshare.client.domain.commando;

public class CommandNotParsableException extends Exception {
	public CommandNotParsableException(String message) {
		super(message + " is not parsable.");
	}

}
