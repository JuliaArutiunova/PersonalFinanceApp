package by.it_academy.jd2.user_service.service;

import by.it_academy.jd2.user_service.dto.*;
import by.it_academy.jd2.user_service.exception.*;
import by.it_academy.jd2.user_service.service.api.IUserService;
import by.it_academy.jd2.user_service.service.api.IVerificationService;
import by.it_academy.jd2.user_service.service.mapper.UserMapper;
import by.it_academy.jd2.user_service.storage.api.IUserStorage;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.entity.UserRole;
import by.it_academy.jd2.user_service.storage.entity.UserStatus;
import by.it_academy.jd2.user_service.storage.entity.VerificationEntity;
import by.it_academy.jd2.user_service.storage.projection.UserLoginProjection;
import by.it_academy.jd2.user_service.storage.projection.UserProjection;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordAlreadyExistException;
import by.it_academy.lib.exception.RecordNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.UUID;


@Service
public class UserService implements IUserService {

    private final IUserStorage userStorage;
    private final IVerificationService verificationService;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;


    public UserService(IUserStorage userStorage, IVerificationService verificationService,
                       UserMapper userMapper, PasswordEncoder encoder) {
        this.userStorage = userStorage;
        this.verificationService = verificationService;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }


    @Override
    @Transactional(readOnly = true)
    public PageDTO<UserDTO> getUsersPage(int pageNumber, int size) {

        Page<UserProjection> page = userStorage.findAllProjectedBy(PageRequest.of(pageNumber, size));

        if (pageNumber > page.getNumber()) {
            throw new PageNotExistException(pageNumber);
        }

        return userMapper.mapPageToDTO(page);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserInfoById(UUID id) {
        UserProjection userProjection = userStorage.findUserProjectionByUserId(id).orElseThrow(() ->
                new UserNotFoundException("Пользователь с id " + id + " Не найден"));
        return userMapper.mapUserProjectionToDTO(userProjection);
    }


    @Override
    @Transactional
    public void save(UserCreateDTO userCreateDTO) {
        String userMail = userCreateDTO.getMail();

        if (userStorage.existsByMail(userMail)) {
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
        userStorage.saveAndFlush(userEntity);

        if (userEntity.getStatus().equals(UserStatus.WAITING_ACTIVATION)) {
                verificationService.create(userEntity);
        }


    }

    @Override
    @Transactional
    public void update(UUID uuid, long dtUpdate, UserCreateDTO userCreateDTO) {
        UserEntity userEntity = userStorage.findById(uuid).orElseThrow(() ->
                new UserNotFoundException("Пользователь с id " + uuid + " Не найден"));

        if (userEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new DataChangedException();
        }

        userEntity.setMail(userCreateDTO.getMail());
        userEntity.setFio(userCreateDTO.getFio());
        userEntity.setRole(UserRole.valueOf(userCreateDTO.getRole()));
        userEntity.setStatus(UserStatus.valueOf(userCreateDTO.getStatus()));
        userEntity.setPassword(encoder.encode(userCreateDTO.getPassword()));

        userStorage.saveAndFlush(userEntity);
    }

    @Override
    @Transactional
    public void verifyUser(String code, String mail) {
        VerificationEntity verification = verificationService.get(mail);
        UserEntity userEntity = verification.getUserEntity();

        if (verification.getCode().equals(code)) {
            userEntity.setStatus(UserStatus.ACTIVATED);
            userStorage.saveAndFlush(userEntity);

            verificationService.delete(verification);

        } else {
            throw new CodeNotValidException();
        }
    }

    @Override
    @Transactional
    public TokenInfoDTO getTokenInfo(UserLoginDTO loginDTO) {
        UserLoginProjection userInfo = userStorage.findUserLoginProjectionByMail(loginDTO.getMail()).orElseThrow(() ->
                new UserNotFoundException("Пользователь с email " + loginDTO.getMail() + " не найден"));

        if (!encoder.matches(loginDTO.getPassword(), userInfo.getPassword())) {
            throw new PasswordNotValidException();
        }

        return new TokenInfoDTO(userInfo.getUserId().toString(), userInfo.getRole().name());
    }



}
