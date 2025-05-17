package exceptions;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException() {
        super("Note %s does not exist!");
    }
}
