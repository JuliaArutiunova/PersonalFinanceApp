package by.it_academy.audit_service.controller;

import by.it_academy.audit_service.dto.AuditDTO;
import by.it_academy.audit_service.service.api.IAuditService;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.PaginationDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final IAuditService auditService;

    public AuditController(IAuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<AuditDTO> getAuditPage(@Valid PaginationDTO paginationDTO) {
        return auditService.getPage(paginationDTO);
    }

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    private AuditDTO getAudit(@PathVariable("uuid") UUID uuid) {
        return auditService.getAudit(uuid);
    }
}
