package be.jprojects.ikshare.client.domain.commando;

public class CommandNotFoundException extends Exception {
	public CommandNotFoundException(String message) {
		super(message + " is not a command.");
	}

}
