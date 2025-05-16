package exceptions;

public class NoteAlreadyExistsException extends RuntimeException {
    public NoteAlreadyExistsException(String name) {
        super(name + " already exists!");
    }
}
