package ro.fiipractic.mycinema.exceptions;

public class NotFoundException extends MyGenericException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}