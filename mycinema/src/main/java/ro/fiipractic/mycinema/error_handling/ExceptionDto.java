package ro.fiipractic.mycinema.error_handling;

public class ExceptionDto {

    private Integer statusCode;

    private String message;

    public ExceptionDto(){};

    public ExceptionDto(Integer statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}