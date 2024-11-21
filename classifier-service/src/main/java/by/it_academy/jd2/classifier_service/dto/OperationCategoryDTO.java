package by.it_academy.jd2.classifier_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class OperationCategoryDTO {
    @JsonProperty(index = 0)
    private UUID uuid;

    @JsonProperty(value = "dt_create", index = 1)
    private long dtCreate;

    @JsonProperty(value = "dt_update", index = 2)
    private long dtUpdate;

    @JsonProperty(index = 3)
    private String title;
}
