package by.it_academy.jd2.user_service.dto.error;

import lombok.Data;

@Data
public class ErrorResponse {
    private ErrorType logref = ErrorType.ERROR;
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
