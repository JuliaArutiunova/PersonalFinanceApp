package by.it_academy.audit_service.service.api;

import by.it_academy.lib.dto.UserInfoDTO;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IClientService {
    UserInfoDTO getUserInfo(UUID userId);

    Map<UUID, UserInfoDTO> getUsersInfo(Set<UUID> users);
}
