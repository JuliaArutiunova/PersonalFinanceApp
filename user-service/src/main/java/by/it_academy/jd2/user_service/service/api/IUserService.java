package by.it_academy.jd2.user_service.service.api;


import by.it_academy.jd2.user_service.dto.UserCreateDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.dto.UserLoginDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.TokenInfoDTO;
import by.it_academy.lib.dto.UserInfoDTO;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IUserService {

    PageDTO<UserDTO> getUsersPage(int pageNumber, int size);

    UserDTO getById(UUID id);

    void create(UserCreateDTO userCreateDTO);

    void update(UUID uuid, long dtUpdate, UserCreateDTO userCreateDTO);

    void verify(String code, String mail);

    TokenInfoDTO getTokenInfo(UserLoginDTO loginDTO);

    UserDTO getMe();

    UserInfoDTO getUserInfo(UUID uuid);

    Map<UUID, UserInfoDTO> getUsersInfo(Set<UUID> uuids);


}
