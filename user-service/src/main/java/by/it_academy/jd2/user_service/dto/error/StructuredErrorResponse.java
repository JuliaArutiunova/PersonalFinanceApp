package by.it_academy.jd2.user_service.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class StructuredErrorResponse {
    private ErrorType logref = ErrorType.STRUCTURED_ERROR;
    @JsonProperty("errors")
    private List<FieldErrorDTO> fieldErrorDTOS;

    public StructuredErrorResponse(List<FieldErrorDTO> fieldErrorDTOS) {
        this.fieldErrorDTOS = fieldErrorDTOS;
    }
}
