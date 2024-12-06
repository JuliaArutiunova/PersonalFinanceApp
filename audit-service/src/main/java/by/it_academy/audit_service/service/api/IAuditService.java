package by.it_academy.audit_service.service.api;


import by.it_academy.audit_service.dto.AuditDTO;
import by.it_academy.lib.dto.AuditCreateDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.PaginationDTO;

import java.util.UUID;

public interface IAuditService {
    void create(AuditCreateDTO auditCreateDTO);

    PageDTO<AuditDTO> getPage(PaginationDTO paginationDTO);

    AuditDTO getAudit(UUID uuid);
}
