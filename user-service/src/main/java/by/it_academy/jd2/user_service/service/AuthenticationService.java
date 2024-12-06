package by.it_academy.jd2.user_service.service;

import by.it_academy.jd2.user_service.controller.utils.JwtTokenHandler;
import by.it_academy.jd2.user_service.dto.UserCreateDTO;
import by.it_academy.jd2.user_service.dto.UserLoginDTO;
import by.it_academy.jd2.user_service.dto.UserRegistrationDTO;
import by.it_academy.jd2.user_service.service.api.IAuthenticationService;
import by.it_academy.jd2.user_service.service.api.IUserService;
import by.it_academy.jd2.user_service.storage.entity.UserRole;
import by.it_academy.jd2.user_service.storage.entity.UserStatus;
import by.it_academy.lib.dto.TokenInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final IUserService userService;


    private final JwtTokenHandler jwtTokenHandler;

    public AuthenticationService(IUserService userService,JwtTokenHandler jwtTokenHandler) {
        this.userService = userService;
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @Override
    public void registerUser(UserRegistrationDTO registrationDTO) {

        userService.create(UserCreateDTO.builder()
                .mail(registrationDTO.getMail())
                .fio(registrationDTO.getFio())
                .password(registrationDTO.getPassword())
                .role(UserRole.USER.name())
                .status(UserStatus.WAITING_ACTIVATION.name())
                .build());

    }

    @Override
    public String getToken(UserLoginDTO loginDTO) {

        TokenInfoDTO tokenInfo = userService.getTokenInfo(loginDTO);

        return  jwtTokenHandler.generateToken(tokenInfo);
    }

}
