package by.it_academy.lib.dto;

import by.it_academy.lib.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {

    private UUID id;

    private String mail;

    private String fio;

    private UserRole role;

}
