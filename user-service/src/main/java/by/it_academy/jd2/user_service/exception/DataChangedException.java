package by.it_academy.jd2.user_service.exception;

public class DataChangedException extends RuntimeException{
    public DataChangedException() {
    }

    public DataChangedException(String message) {
        super(message);
    }
}
