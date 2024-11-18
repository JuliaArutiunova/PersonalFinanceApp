package by.it_academy.jd2.user_service.storage.projection;

import by.it_academy.jd2.user_service.storage.entity.UserRole;
import by.it_academy.jd2.user_service.storage.entity.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserProjection {
    UUID getUserId();

    LocalDateTime getDtCreate();

    LocalDateTime getDtUpdate();

    String getMail();

    String getFio();

    UserRole getRole();

    UserStatus getStatus();
}
