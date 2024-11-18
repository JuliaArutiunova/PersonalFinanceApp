package by.it_academy.jd2.user_service.dto;

import by.it_academy.jd2.user_service.storage.entity.UserRole;
import by.it_academy.jd2.user_service.storage.entity.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class UserDTO {
    @JsonProperty(index = 0)
    private UUID id;

    @JsonProperty(value = "dt_create", index = 1)
    private long dtCreate;

    @JsonProperty(value = "dt_update", index = 2)
    private long dtUpdate;

    @JsonProperty(index = 3)
    private String mail;

    @JsonProperty(index = 4)
    private String fio;

    @JsonProperty(index = 5)
    private UserRole role;

    @JsonProperty(index = 6)
    private UserStatus status;
}
