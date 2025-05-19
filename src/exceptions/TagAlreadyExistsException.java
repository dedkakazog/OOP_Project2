package exceptions;

public class TagAlreadyExistsException extends RuntimeException {
    public TagAlreadyExistsException() {
        super("Note %s is already tagged with %s!");
    }
}
