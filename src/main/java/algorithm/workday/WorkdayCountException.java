package algorithm.workday;

/**
 * @author by chenyi
 * @date 2021/8/2
 */
public class WorkdayCountException extends RuntimeException {

    public WorkdayCountException() {
    }

    public WorkdayCountException(String message) {
        super(message);
    }

    public WorkdayCountException(String message, Throwable cause) {
        super(message, cause);
    }
}
