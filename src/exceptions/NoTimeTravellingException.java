package exceptions;

public class NoTimeTravellingException extends RuntimeException {
    public NoTimeTravellingException(String message) {
        super(message);
    }
}
