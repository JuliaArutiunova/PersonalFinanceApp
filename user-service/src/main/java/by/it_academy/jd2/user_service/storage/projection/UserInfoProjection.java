package by.it_academy.jd2.user_service.storage.projection;

import by.it_academy.jd2.user_service.storage.entity.UserRole;

import java.util.UUID;

public interface UserInfoProjection {
    UUID getUserId();

    String getMail();

    String getFio();

    UserRole getRole();
}
