package by.it_academy.lib.exception;

public class RecordAlreadyExistException extends RuntimeException {
    private RecordAlreadyExistException(){}
    public RecordAlreadyExistException(String message) {
        super(message);
    }
}
