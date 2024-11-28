package by.it_academy.jd2.service;

import by.it_academy.jd2.dao.api.IUserDao;
import by.it_academy.jd2.dao.entity.UserIdEntity;
import by.it_academy.jd2.service.api.IUserService;
import by.it_academy.lib.exception.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class UserService implements IUserService {
    private final IUserDao userDao;

    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserIdEntity getUserIdEntity(UUID uuid) {
        return userDao.findById(uuid).orElseThrow(() ->
                new RecordNotFoundException("Пользователь не найден, обратитесь в службу поддержки"));
    }

    @Override
    public void save(UUID uuid) {
        UserIdEntity user = new UserIdEntity(uuid);
        userDao.saveAndFlush(user);
    }
}
