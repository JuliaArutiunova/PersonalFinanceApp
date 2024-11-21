package by.it_academy.jd2.user_service.service.api;

import by.it_academy.jd2.user_service.dto.TokenInfoDTO;
import by.it_academy.jd2.user_service.dto.UserCreateDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.dto.UserLoginDTO;
import by.it_academy.lib.dto.PageDTO;

import java.util.UUID;

public interface IUserService {

    PageDTO<UserDTO> getUsersPage(int pageNumber, int size);

    UserDTO getUserInfoById(UUID id);

    void save(UserCreateDTO userCreateDTO);

    void update(UUID uuid, long dtUpdate, UserCreateDTO userCreateDTO);

    void verifyUser(String code, String mail);

    TokenInfoDTO getTokenInfo(UserLoginDTO loginDTO);




}
