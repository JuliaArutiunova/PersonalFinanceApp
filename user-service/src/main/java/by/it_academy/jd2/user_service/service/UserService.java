package by.it_academy.jd2.user_service.service;


import by.it_academy.jd2.user_service.dto.UserCreateDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.dto.UserLoginDTO;
import by.it_academy.jd2.user_service.exception.ActivationException;
import by.it_academy.jd2.user_service.exception.CodeNotValidException;
import by.it_academy.jd2.user_service.service.api.IClientService;
import by.it_academy.jd2.user_service.storage.projection.UserInfoProjection;
import by.it_academy.lib.dto.AuditCreateDTO;
import by.it_academy.lib.dto.UserInfoDTO;
import by.it_academy.lib.enums.EssenceType;
import by.it_academy.lib.exception.DataChangedException;
import by.it_academy.jd2.user_service.exception.PasswordNotValidException;
import by.it_academy.jd2.user_service.service.api.IUserService;
import by.it_academy.jd2.user_service.service.api.IVerificationService;
import by.it_academy.jd2.user_service.storage.api.IUserDAO;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.entity.UserRole;
import by.it_academy.jd2.user_service.storage.entity.UserStatus;
import by.it_academy.jd2.user_service.storage.entity.VerificationEntity;
import by.it_academy.jd2.user_service.storage.projection.UserLoginProjection;
import by.it_academy.jd2.user_service.storage.projection.UserProjection;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.TokenInfoDTO;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordAlreadyExistException;
import by.it_academy.lib.exception.RecordNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.*;


@Service
public class UserService implements IUserService {

    private final IUserDAO userStorage;
    private final IVerificationService verificationService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final UserHolder userHolder;
    private final IClientService clientService;


    public UserService(IUserDAO userStorage, IVerificationService verificationService,
                       ModelMapper modelMapper, PasswordEncoder encoder, UserHolder userHolder, IClientService clientService) {
        this.userStorage = userStorage;
        this.verificationService = verificationService;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
        this.userHolder = userHolder;
        this.clientService = clientService;
    }


    @Override
    @Transactional(readOnly = true)
    public PageDTO<UserDTO> getUsersPage(int pageNumber, int size) {

        Page<UserProjection> page = userStorage.findAllProjectedBy(PageRequest.of(pageNumber, size));

        if (pageNumber > page.getTotalPages() - 1) {
            throw new PageNotExistException("Страницы с номером " + pageNumber + " не существует");
        }

        return modelMapper.map(page, new TypeToken<PageDTO<UserDTO>>() {
        }.getType());
    }


    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserInfoById(UUID id) {

        UserProjection userProjection = userStorage.findUserProjectionByUserId(id).orElseThrow(() ->
                new RecordNotFoundException("Пользователь не найден"));

        return modelMapper.map(userProjection, UserDTO.class);
    }


    @Override
    @Transactional
    public void save(UserCreateDTO userCreateDTO) {

        String userMail = userCreateDTO.getMail();

        if (userStorage.existsByMail(userMail)) {
            throw new RecordAlreadyExistException("Пользователь с email " + userCreateDTO.getMail()
                    + " уже существует");
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

        AuditCreateDTO auditCreateDTO = new AuditCreateDTO();
        auditCreateDTO.setText("Создан новый пользователь");
        auditCreateDTO.setEntityId(userEntity.getUserId());
        auditCreateDTO.setEssenceType(EssenceType.USER);
        if (userHolder.getUserId() == null) {
            auditCreateDTO.setUser(userEntity.getUserId());
        } else {
            auditCreateDTO.setUser(userHolder.getUserId());
        }
        clientService.toAudit(auditCreateDTO);


    }

    @Override
    @Transactional
    public void update(UUID uuid, long dtUpdate, UserCreateDTO userCreateDTO) {

        UserEntity userEntity = userStorage.findById(uuid).orElseThrow(() ->
                new RecordNotFoundException("Пользователь не найден"));

        if (userEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new DataChangedException();
        }

        userEntity.setMail(userCreateDTO.getMail());
        userEntity.setFio(userCreateDTO.getFio());
        userEntity.setRole(UserRole.valueOf(userCreateDTO.getRole()));
        userEntity.setStatus(UserStatus.valueOf(userCreateDTO.getStatus()));
        userEntity.setPassword(encoder.encode(userCreateDTO.getPassword()));

        userStorage.saveAndFlush(userEntity);

        clientService.toAudit(AuditCreateDTO.builder()
                .user(userHolder.getUserId())
                .text("Отредактирована информация о пользователе")
                .entityId(uuid)
                .essenceType(EssenceType.USER)
                .build());
    }

    @Override
    @Transactional
    public void verify(String code, String mail) {

        if (!userStorage.existsByMail(mail)) {
            throw new RecordNotFoundException("Пользователь с email " + mail + " не найден");
        }

        VerificationEntity verification = verificationService.get(mail);

        if (verification.getCode().equals(code)) {
            UserEntity userEntity = verification.getUserEntity();
            userEntity.setStatus(UserStatus.ACTIVATED);
            userStorage.saveAndFlush(userEntity);
            verificationService.delete(verification);

        } else {
            throw new CodeNotValidException("Неверный код верификации");
        }
    }

    @Override
    @Transactional
    public TokenInfoDTO getTokenInfo(UserLoginDTO loginDTO) {

        UserLoginProjection userInfo = userStorage.findUserLoginProjectionByMail(loginDTO.getMail()).orElseThrow(() ->
                new RecordNotFoundException("Пользователь с email " + loginDTO.getMail() + " не найден"));

        if (!userInfo.getStatus().equals(UserStatus.ACTIVATED)) {
            throw new ActivationException("Аккаунт не активирован. Необходимо пройти верификацию");
        }

        if (!encoder.matches(loginDTO.getPassword(), userInfo.getPassword())) {
            throw new PasswordNotValidException("Неверный пароль");
        }

        clientService.toAudit(AuditCreateDTO.builder()
                .user(userInfo.getUserId())
                .text("Пользователь вошел в систему")
                .entityId(userInfo.getUserId())
                .essenceType(EssenceType.USER)
                .build());

        return new TokenInfoDTO(userInfo.getUserId().toString(), userInfo.getRole().name());
    }


    @Override
    @Transactional(readOnly = true)
    public UserInfoDTO getUserInfo(UUID uuid) {

        UserInfoProjection userInfoProjection = userStorage.findUserInfoProjectionByUserId(uuid);

        return modelMapper.map(userInfoProjection, UserInfoDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, UserInfoDTO> getUsersInfo(Set<UUID> uuids) {
        Map<UUID, UserInfoDTO> userInfoDTOMap = new HashMap<>();

        userStorage.findAllProjectedByUserIdIn(uuids).forEach(userInfoProjection ->
                userInfoDTOMap.put(userInfoProjection.getUserId(),
                        modelMapper.map(userInfoProjection, UserInfoDTO.class)));

        return userInfoDTOMap;
    }


}
