package by.it_academy.jd2.user_service.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CodeNotValidException extends RuntimeException {
    String message = "Неверный код";

    public CodeNotValidException(String message) {
        this.message = message;
    }
}
