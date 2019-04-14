package ro.fiipractic.mycinema.exceptions;

public class BadRequestException extends MyGenericException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}