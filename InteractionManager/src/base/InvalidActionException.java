package base;

public class InvalidActionException extends RuntimeException {

	private static final long serialVersionUID = 4178140449738156607L;

	public InvalidActionException(String action) {
		super(action + " is not a valid action.");
	}
	
}
