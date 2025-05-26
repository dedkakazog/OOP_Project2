package exceptions;

public class UnknownNoteKindException extends RuntimeException {
    public UnknownNoteKindException() {
        super("Unknown note kind!%n");
    }
}
