package by.it_academy.jd2.user_service.service.api;

import by.it_academy.jd2.user_service.dto.UserLoginDTO;
import by.it_academy.jd2.user_service.dto.UserRegistrationDTO;

public interface IAuthenticationService {
    void registerUser(UserRegistrationDTO registrationDTO);

    String getToken(UserLoginDTO loginDTO);

}
