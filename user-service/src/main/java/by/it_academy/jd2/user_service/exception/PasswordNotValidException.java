package by.it_academy.jd2.user_service.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PasswordNotValidException extends RuntimeException{
    String message = "Неверный пароль";

    public PasswordNotValidException(String message) {
        this.message = message;

    }
}
