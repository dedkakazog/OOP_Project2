package exceptions;

public class StartEndDateException extends RuntimeException {
    public StartEndDateException() {
        super("The ending date must not precede the starting date!");
    }
}
