package by.it_academy.jd2.user_service.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MailAlreadyExistException extends RuntimeException {
    private String email;

    public MailAlreadyExistException(String email) {
        this.email = email;
    }
}
