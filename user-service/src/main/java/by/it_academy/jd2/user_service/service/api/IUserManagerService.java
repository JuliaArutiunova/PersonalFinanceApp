package by.it_academy.jd2.user_service.service.api;

import by.it_academy.jd2.user_service.dto.PageDTO;
import by.it_academy.jd2.user_service.dto.PaginationDTO;
import by.it_academy.jd2.user_service.dto.UserCreateDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;

import java.util.UUID;

public interface IUserManagerService {
    void createUser(UserCreateDTO userCreateDTO);

    PageDTO<UserDTO> getUsersPage(PaginationDTO paginationDTO);

    UserDTO getById(UUID id);

    void updateUser(UUID uuid, long dtUpdate, UserCreateDTO userCreateDTO);


}
