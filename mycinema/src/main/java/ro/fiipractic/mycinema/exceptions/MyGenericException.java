package ro.fiipractic.mycinema.exceptions;

public class MyGenericException extends Exception {

    public MyGenericException(String message){ super(message); }

    public MyGenericException(String message, Throwable cause){
        super(message, cause);
    }
}