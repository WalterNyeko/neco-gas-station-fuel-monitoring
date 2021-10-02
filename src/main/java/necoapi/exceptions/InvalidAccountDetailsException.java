package necoapi.exceptions;

public class InvalidAccountDetailsException extends RuntimeException{
    public InvalidAccountDetailsException(String message) {
        super(message);
    }
}
