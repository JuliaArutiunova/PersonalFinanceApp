package by.it_academy.jd2.user_service.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ErrorType {
    @JsonProperty("error")
    ERROR,
    @JsonProperty("structured_error")
    STRUCTURED_ERROR
}
