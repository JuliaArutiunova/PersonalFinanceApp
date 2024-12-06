package by.it_academy.audit_service.controller;

import by.it_academy.audit_service.service.api.IAuditService;
import by.it_academy.lib.dto.AuditCreateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/audit_data")
public class DataController {

    private final IAuditService auditService;

    public DataController(IAuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void getAudit(@RequestBody AuditCreateDTO auditCreateDTO) {
        auditService.create(auditCreateDTO);
    }

}
