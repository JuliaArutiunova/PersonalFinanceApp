package by.it_academy.audit_service.mapper;

import by.it_academy.audit_service.dao.entity.AuditEntity;
import by.it_academy.audit_service.dto.AuditDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.UserInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class AuditMapper {

    public PageDTO<AuditDTO> mapAuditPage(Page<AuditEntity> page, Map<UUID, UserInfoDTO> usersInfo){
        PageDTO<AuditDTO> auditDTOPage = new PageDTO<>();
        auditDTOPage.setNumber(page.getNumber());
        auditDTOPage.setTotalPages(page.getTotalPages());
        auditDTOPage.setNumberOfElements(page.getNumberOfElements());
        auditDTOPage.setTotalElements(page.getTotalElements());
        auditDTOPage.setSize(page.getSize());
        auditDTOPage.setFirst(page.isFirst());
        auditDTOPage.setLast(page.isLast());

        List<AuditDTO> auditDTOS = page.getContent().stream().map(auditEntity ->
                AuditDTO.builder()
                        .auditId(auditEntity.getAuditId())
                        .dtCreate(auditEntity.getDtCreate().toEpochSecond(ZoneOffset.UTC))
                        .dtUpdate(auditEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC))
                        .user(usersInfo.get(auditEntity.getUser()))
                        .text(auditEntity.getText())
                        .type(auditEntity.getType())
                        .entityId(auditEntity.getEntityId())
                        .build()).toList();
        auditDTOPage.setContent(auditDTOS);
        return auditDTOPage;
    }

    public AuditDTO mapAuditDto(AuditEntity auditEntity, UserInfoDTO userInfo){
        return AuditDTO.builder()
                .auditId(auditEntity.getAuditId())
                .dtCreate(auditEntity.getDtCreate().toEpochSecond(ZoneOffset.UTC))
                .dtUpdate(auditEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC))
                .user(userInfo)
                .text(auditEntity.getText())
                .type(auditEntity.getType())
                .entityId(auditEntity.getEntityId())
                .build();
    }
}
