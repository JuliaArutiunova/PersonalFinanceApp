package by.it_academy.jd2.service.api;

import by.it_academy.jd2.dao.entity.OperationEntity;
import by.it_academy.jd2.dto.OperationCreateDTO;
import by.it_academy.jd2.dto.OperationDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.PaginationDTO;

import java.util.UUID;

public interface IOperationService {

    void create(UUID accountId, OperationCreateDTO operationCreateDTO);

    PageDTO<OperationDTO> get(UUID accountId,PaginationDTO paginationDTO);

    OperationEntity get(UUID uuid);

    void update(UUID accountId, UUID operationId, long dtUpdate, OperationCreateDTO operationCreateDTO);

    void delete(UUID accountId, UUID operationId, long dtUpdate);
}
