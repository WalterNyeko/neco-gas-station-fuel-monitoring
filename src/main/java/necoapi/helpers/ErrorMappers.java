package necoapi.helpers;

public class ErrorMappers {
    public static APIError mapErrors(String message, String reason) {
        return new APIError(message, reason);
    }
}
