package exceptions;

import java.time.DateTimeException;

public class InvalidStartDateException extends DateTimeException {
    public InvalidStartDateException() {
        super("Invalid start date!%n");
    }
}
