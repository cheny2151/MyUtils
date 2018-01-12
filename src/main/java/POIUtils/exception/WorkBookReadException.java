package POIUtils.exception;

public class WorkBookReadException extends RuntimeException {

    public WorkBookReadException(String message) {
        super(message);
    }

    public WorkBookReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
