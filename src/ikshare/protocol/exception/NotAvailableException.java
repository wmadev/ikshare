package ikshare.protocol.exception;

public class NotAvailableException extends Exception {
	public NotAvailableException(String message) {
		super(message + " is not available.");
	}

}
