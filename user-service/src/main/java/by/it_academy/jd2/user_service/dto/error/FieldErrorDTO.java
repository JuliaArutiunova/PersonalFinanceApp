package by.it_academy.jd2.user_service.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorDTO {
    private String message;
    private String field;
}
