package by.it_academy.jd2.user_service.dto;

import by.it_academy.jd2.user_service.storage.entity.UserRole;
import by.it_academy.jd2.user_service.storage.entity.UserStatus;
import by.it_academy.jd2.user_service.validation.EnumValue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserCreateDTO {

    @NotBlank(message = "Поле не заполнено")
    @Email(message = "Некорректный email")
    private String mail;

    @NotBlank(message = "Поле не заполнено")
    private String fio;

    @NotBlank(message = "Поле не заполнено")
    @EnumValue(enumClass = UserRole.class)
    private String role;

    @NotBlank(message = "Поле не заполнено")
    @EnumValue(enumClass = UserStatus.class)
    private String status;

    @NotBlank(message = "Поле не заполнено")
    private String password;

}
