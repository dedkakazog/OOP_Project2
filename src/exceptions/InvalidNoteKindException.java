package exceptions;

public class InvalidNoteKindException extends RuntimeException {
    public InvalidNoteKindException(String message) {
        super(message);
    }

}
