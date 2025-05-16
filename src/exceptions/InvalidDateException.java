package exceptions;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException() {
        super("Invalid date!");
    }
}
