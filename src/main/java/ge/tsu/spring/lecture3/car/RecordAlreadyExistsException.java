package ge.tsu.spring.lecture3.car;

public class RecordAlreadyExistsException extends Exception {

    public RecordAlreadyExistsException(String message) {
        super(message);
    }

    public RecordAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
