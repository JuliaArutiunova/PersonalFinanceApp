package by.it_academy.lib.exception;

public class PageNotExistException extends RuntimeException{

    public PageNotExistException(){}

    public PageNotExistException(String message) {
        super(message);
    }
}
