package by.it_academy.audit_service.service;

import by.it_academy.audit_service.dao.api.IAuditDao;
import by.it_academy.audit_service.dao.entity.AuditEntity;
import by.it_academy.audit_service.dto.AuditDTO;
import by.it_academy.audit_service.mapper.AuditMapper;
import by.it_academy.audit_service.service.api.IAuditService;
import by.it_academy.lib.dto.AuditCreateDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.PaginationDTO;
import by.it_academy.lib.dto.UserInfoDTO;
import by.it_academy.lib.exception.RecordNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;


@Service
public class AuditService implements IAuditService {

    private final IAuditDao auditDao;
    private final AuditMapper auditMapper;
    private final ClientService clientService;

    public AuditService(IAuditDao auditDao, AuditMapper auditMapper, ClientService clientService) {
        this.auditDao = auditDao;
        this.auditMapper = auditMapper;
        this.clientService = clientService;
    }

    @Override
    @Transactional
    public void create(AuditCreateDTO auditCreateDTO) {
        AuditEntity auditEntity = new AuditEntity();

        auditEntity.setAuditId(UUID.randomUUID());
        auditEntity.setUser(auditCreateDTO.getUser());
        auditEntity.setText(auditCreateDTO.getText());
        auditEntity.setType(auditCreateDTO.getEssenceType());
        auditEntity.setEntityId(auditCreateDTO.getEntityId());

        auditDao.saveAndFlush(auditEntity);

    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<AuditDTO> getPage(PaginationDTO paginationDTO) {        //TODO
        Page<AuditEntity> page =
                auditDao.findAll(PageRequest.of(paginationDTO.getPage(), paginationDTO.getSize()));

        Set<UUID> uuids = new HashSet<>();
        page.getContent().forEach(auditEntity -> uuids.add(auditEntity.getUser()));

        Map<UUID, UserInfoDTO> usersInfo = clientService.getUsersInfo(uuids);

        return auditMapper.mapAuditPage(page, usersInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditDTO getAudit(UUID uuid) {                                   //TODO
        AuditEntity auditEntity = auditDao.findById(uuid).orElseThrow(() ->
                new RecordNotFoundException("Запись не найдена"));

        UserInfoDTO userInfo = clientService.getUserInfo(auditEntity.getUser());

        return auditMapper.mapAuditDto(auditEntity, userInfo);
    }


}
