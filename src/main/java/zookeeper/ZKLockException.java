package zookeeper;

public class ZKLockException extends RuntimeException {

    public ZKLockException() {
    }

    public ZKLockException(String message) {
        super(message);
    }
}
