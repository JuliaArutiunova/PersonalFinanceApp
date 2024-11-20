package by.it_academy.jd2.user_service.service.api;

import by.it_academy.jd2.user_service.dto.*;

import java.util.UUID;

public interface IUserService {

    PageDTO<UserDTO> getUsersPage(int pageNumber, int size);

    UserDTO getUserInfoById(UUID id);

    void save(UserCreateDTO userCreateDTO);

    void update(UUID uuid, long dtUpdate, UserCreateDTO userCreateDTO);

    void verifyUser(String code, String mail);

    TokenInfoDTO getTokenInfo(UserLoginDTO loginDTO);




}
