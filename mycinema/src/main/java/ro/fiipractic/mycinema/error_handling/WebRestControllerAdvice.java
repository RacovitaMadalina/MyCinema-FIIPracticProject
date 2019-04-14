package ro.fiipractic.mycinema.error_handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.fiipractic.mycinema.exceptions.BadRequestException;
import ro.fiipractic.mycinema.exceptions.NotFoundException;

@RestControllerAdvice
public class WebRestControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleBadRequestException(BadRequestException ex){
        return this.generateErrorDto(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleNotFoundException(NotFoundException ex){
        return this.generateErrorDto(HttpStatus.NOT_FOUND, ex);
    }

    /**
     * Default handler for exceptions that may occur in the application.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto defaultHandler(Exception ex){
        return this.generateErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ExceptionDto generateErrorDto(HttpStatus status, Exception ex) {
        return new ExceptionDto(status.value(), ex.getMessage());
    }
}