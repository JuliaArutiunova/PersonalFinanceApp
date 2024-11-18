package by.it_academy.jd2.user_service.service;

import by.it_academy.jd2.user_service.dto.PageDTO;
import by.it_academy.jd2.user_service.dto.PaginationDTO;
import by.it_academy.jd2.user_service.dto.UserCreateDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.exception.DataChangedException;
import by.it_academy.jd2.user_service.exception.MailAlreadyExistException;
import by.it_academy.jd2.user_service.service.api.IUserManagerService;
import by.it_academy.jd2.user_service.service.api.IUserService;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.entity.UserRole;
import by.it_academy.jd2.user_service.storage.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class UserManagerService implements IUserManagerService {
    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void createUser(UserCreateDTO userCreateDTO) {
        String userMail = userCreateDTO.getMail();
        if (userService.isExists(userMail)) {
            throw new MailAlreadyExistException(userCreateDTO.getMail());
        }
        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .fio(userCreateDTO.getFio())
                .mail(userMail)
                .password(encoder.encode(userCreateDTO.getPassword()))
                .status(UserStatus.valueOf(userCreateDTO.getStatus()))
                .role(UserRole.valueOf(userCreateDTO.getRole()))
                .build();
        if (userEntity.getStatus().equals(UserStatus.WAITING_ACTIVATION)) {
            String code = userService.generateActivationCode();
            userEntity.setActivationCode(code);
            userService.sendVerification(userEntity.getFio(), userMail, code);
        }
        userService.saveUser(userEntity);

    }

    @Override
    public PageDTO<UserDTO> getUsersPage(PaginationDTO paginationDTO) {
        return userService.getUsersPage(paginationDTO.getPage(), paginationDTO.getSize());
    }

    @Override
    public UserDTO getById(UUID id) {
        return userService.getUserInfoById(id);
    }


    @Override
    @Transactional
    public void updateUser(UUID uuid, long dtUpdate, UserCreateDTO userCreateDTO) {

        UserEntity userEntity = userService.getById(uuid);

        if (userEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new DataChangedException();
        }

        userEntity.setMail(userCreateDTO.getMail());
        userEntity.setFio(userCreateDTO.getFio());
        userEntity.setRole(UserRole.valueOf(userCreateDTO.getRole()));
        userEntity.setStatus(UserStatus.valueOf(userCreateDTO.getStatus()));
        userEntity.setPassword(encoder.encode(userCreateDTO.getPassword()));

        userService.saveUser(userEntity);

    }
}
