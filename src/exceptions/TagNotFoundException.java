package exceptions;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException() {
        super("Note on %s is not tagged with %s!%n");
    }
}
