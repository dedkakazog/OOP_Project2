package exceptions;

public class NoTimeTravellingDocumentException extends RuntimeException {
    public NoTimeTravellingDocumentException() {
        super("No time travelling to the future!");
    }

}
