package by.it_academy.jd2.service.api;

import by.it_academy.jd2.dao.entity.UserIdEntity;

import java.util.UUID;

public interface IUserService {
    UserIdEntity getUserIdEntity(UUID uuid);
    void save(UUID uuid);
}
