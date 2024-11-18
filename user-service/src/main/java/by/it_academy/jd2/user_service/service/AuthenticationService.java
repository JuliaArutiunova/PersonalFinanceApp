package by.it_academy.jd2.user_service.service;

import by.it_academy.jd2.user_service.dto.TokenInfoDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.dto.UserLoginDTO;
import by.it_academy.jd2.user_service.dto.UserRegistrationDTO;
import by.it_academy.jd2.user_service.exception.CodeNotValidException;
import by.it_academy.jd2.user_service.exception.MailAlreadyExistException;
import by.it_academy.jd2.user_service.exception.PasswordNotValidException;
import by.it_academy.jd2.user_service.service.api.IAuthenticationService;
import by.it_academy.jd2.user_service.service.api.IUserService;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.entity.UserRole;
import by.it_academy.jd2.user_service.storage.entity.UserStatus;
import by.it_academy.jd2.user_service.storage.projection.UserLoginProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class AuthenticationService implements IAuthenticationService {
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder encoder;
    private UserHolder userHolder;


    @Override
    public void registerUser(UserRegistrationDTO registrationDTO) {
        String userMail = registrationDTO.getMail();

        if (userService.isExists(userMail)) {
            throw new MailAlreadyExistException(userMail);
        }

        String code = userService.generateActivationCode();

        UserEntity userEntity = UserEntity.builder()
                .userId(UUID.randomUUID())
                .fio(registrationDTO.getFio())
                .mail(userMail)
                .password(encoder.encode(registrationDTO.getPassword()))
                .status(UserStatus.WAITING_ACTIVATION)
                .role(UserRole.USER)
                .dtCreate(new Timestamp(System.currentTimeMillis()))
                .dtUpdate(new Timestamp(System.currentTimeMillis()))
                .activationCode(code)
                .build();

        userService.saveUser(userEntity);

        userService.sendVerification(userEntity.getFio(), userMail, code);


    }

    @Override
    public void verifyUser(String code, String mail) { //TODO

        UserEntity userEntity = userService.getByMail(mail);

        if (userEntity.getActivationCode().equals(code)) {
            userEntity.setStatus(UserStatus.ACTIVATED);
            userEntity.setDtUpdate(new Timestamp(System.currentTimeMillis()));
            userService.saveUser(userEntity);
        } else {
            throw new CodeNotValidException();
        }

    }

    @Override
    public TokenInfoDTO login(UserLoginDTO loginDTO) {

        UserLoginProjection userInfo = userService.getUserLoginInfo(loginDTO.getMail());

        if (!encoder.matches(loginDTO.getPassword(), userInfo.getPassword())) {
            throw new PasswordNotValidException();
        }

        return new TokenInfoDTO(userInfo.getUserId().toString(), userInfo.getRole().name());
    }

    @Override
    public UserDTO getMe() {

        UUID id = userHolder.getUserId();

        return userService.getUserInfoById(id);
    }
}
