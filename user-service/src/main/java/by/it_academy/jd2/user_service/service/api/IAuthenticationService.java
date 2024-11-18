package by.it_academy.jd2.user_service.service.api;

import by.it_academy.jd2.user_service.dto.TokenInfoDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.dto.UserLoginDTO;
import by.it_academy.jd2.user_service.dto.UserRegistrationDTO;

public interface IAuthenticationService {
    void registerUser(UserRegistrationDTO registrationDTO);

    void verifyUser(String code, String mail);

    TokenInfoDTO login(UserLoginDTO loginDTO);

    UserDTO getMe();
}
