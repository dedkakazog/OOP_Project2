package exceptions;

import java.time.DateTimeException;

public class InvalidEndDateException extends DateTimeException {
    public InvalidEndDateException() {
        super("Invalid end date!%n");
    }
}
