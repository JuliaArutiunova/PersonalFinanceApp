package by.it_academy.jd2.user_service.service.api;

import by.it_academy.jd2.user_service.dto.PageDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.projection.UserLoginProjection;

import java.util.UUID;

public interface IUserService {

    PageDTO<UserDTO> getUsersPage(int pageNumber, int size);

    UserDTO getUserInfoById(UUID id);

    UserEntity getById(UUID id);

    UserEntity getByMail(String mail);

    void saveUser(UserEntity userEntity);

    boolean isExists(String mail);

    void sendVerification(String userName, String mail, String code);

    UserLoginProjection getUserLoginInfo(String mail);

    String generateActivationCode();


}
