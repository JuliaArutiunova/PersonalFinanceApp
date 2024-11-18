package by.it_academy.jd2.user_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserNotFoundException extends RuntimeException {
    private String message;



}
