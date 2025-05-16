package exceptions;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException() {
        super("%s does not exist!");
    }
}
