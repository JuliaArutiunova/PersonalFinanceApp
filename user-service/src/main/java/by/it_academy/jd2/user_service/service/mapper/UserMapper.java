package by.it_academy.jd2.user_service.service.mapper;

import by.it_academy.jd2.user_service.dto.PageDTO;
import by.it_academy.jd2.user_service.dto.UserDTO;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
@Component
public class UserMapper {

    public UserDTO mapEntityToDto(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getUserId())
                .dtCreate(userEntity.getDtCreate().toEpochSecond(ZoneOffset.UTC))
                .dtUpdate(userEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC))
                .mail(userEntity.getMail())
                .fio(userEntity.getFio())
                .role(userEntity.getRole())
                .status(userEntity.getStatus())
                .build();

    }


    public UserDTO mapUserProjectionToDTO(UserProjection userProjection) {
        return UserDTO.builder()
                .id(userProjection.getUserId())
                .fio(userProjection.getFio())
                .mail(userProjection.getMail())
                .dtCreate(userProjection.getDtCreate().toEpochSecond(ZoneOffset.UTC))
                .dtUpdate(userProjection.getDtUpdate().toEpochSecond(ZoneOffset.UTC))
                .role(userProjection.getRole())
                .status(userProjection.getStatus())
                .build();
    }

    public PageDTO<UserDTO> mapPageToDTO(Page<UserProjection> page){
        PageDTO<UserDTO> usersPageDTO = new PageDTO<>();

        usersPageDTO.setNumber(page.getNumber());
        usersPageDTO.setSize(page.getSize());
        usersPageDTO.setTotalPages(page.getTotalPages());
        usersPageDTO.setTotalElements(page.getTotalElements());
        usersPageDTO.setFirst(page.isFirst());
        usersPageDTO.setLast(page.isLast());
        usersPageDTO.setNumberOfElements(page.getNumberOfElements());

        List<UserDTO> userDTOS = new ArrayList<>();
        page.getContent().forEach(userProjectionProjection -> {
            userDTOS.add(mapUserProjectionToDTO(userProjectionProjection));
        });

        usersPageDTO.setContent(userDTOS);
        return usersPageDTO;
    }


}
