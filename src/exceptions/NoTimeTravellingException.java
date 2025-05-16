package exceptions;

public class NoTimeTravellingException extends RuntimeException {
    public NoTimeTravellingException() {
        super("No time travelling!");
    }
}
