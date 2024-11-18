package by.it_academy.jd2.user_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataChangedException extends RuntimeException{
    String message;
    public DataChangedException(String message) {
        this.message = message;
    }
}
