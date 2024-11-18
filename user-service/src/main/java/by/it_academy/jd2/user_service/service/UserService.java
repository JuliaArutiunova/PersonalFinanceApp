package by.it_academy.jd2.user_service.service;

import by.it_academy.jd2.user_service.dto.PageDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.exception.PageNotExistException;
import by.it_academy.jd2.user_service.exception.UserNotFoundException;
import by.it_academy.jd2.user_service.service.api.IUserService;
import by.it_academy.jd2.user_service.service.mapper.UserMapper;
import by.it_academy.jd2.user_service.service.utils.Generator;
import by.it_academy.jd2.user_service.storage.api.IUserStorage;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.projection.UserLoginProjection;
import by.it_academy.jd2.user_service.storage.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserService implements IUserService {

    private final IUserStorage userStorage;
    private final UserMapper userMapper;
    private final MailSenderService mailSenderService;

    private final Generator generator;

    public UserService(IUserStorage userStorage, UserMapper userMapper,
                       MailSenderService mailSenderService, Generator generator) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
        this.mailSenderService = mailSenderService;
        this.generator = generator;
    }


    @Override
    public PageDTO<UserDTO> getUsersPage(int pageNumber, int size) {

        Page<UserProjection> page = userStorage.findAllProjectedBy(PageRequest.of(pageNumber, size));

        if (pageNumber > page.getTotalPages() - 1) {
            throw new PageNotExistException(pageNumber);
        }

        return userMapper.mapPageToDTO(page);
    }

    @Override
    public UserDTO getUserInfoById(UUID id) {
        UserProjection userProjection = userStorage.findUserProjectionByUserId(id).orElseThrow(() ->
                new UserNotFoundException("Пользователь с id " + id + " Не найден"));
        return userMapper.mapUserProjectionToDTO(userProjection);
    }

    @Override
    public UserEntity getById(UUID id) {
        return userStorage.getReferenceById(id);
    }

    @Override
    public UserEntity getByMail(String mail) {
        return userStorage.findByMail(mail).orElseThrow(() ->
                new UserNotFoundException("Пользователь с email " + mail + " не найден"));
    }


    @Override
    public void saveUser(UserEntity userEntity) {
        userStorage.saveAndFlush(userEntity);
    }

    @Override
    public boolean isExists(String mail) {
        return userStorage.existsByMail(mail);
    }

    @Override
    public void sendVerification(String userName, String mail, String code) {
        String messageText = generator.generateMessageText(userName, mail, code);
        mailSenderService.sendMail(mail, messageText);
    }

    @Override
    public UserLoginProjection getUserLoginInfo(String mail) {
        return userStorage.findUserLoginProjectionByMail(mail).orElseThrow(() ->
                new UserNotFoundException("Пользователь с email " + mail + " не найден"));
    }

    @Override
    public String generateActivationCode() {
        return generator.generateCode();
    }


}
