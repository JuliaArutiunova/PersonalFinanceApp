package by.it_academy.jd2.user_service.storage.projection;

import by.it_academy.jd2.user_service.storage.entity.UserRole;
import by.it_academy.jd2.user_service.storage.entity.UserStatus;

import java.util.UUID;

public interface UserLoginProjection {
    UUID getUserId();

    String getPassword();

    UserRole getRole();

    UserStatus getStatus();
}
