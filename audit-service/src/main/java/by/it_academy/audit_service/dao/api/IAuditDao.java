package by.it_academy.audit_service.dao.api;

import by.it_academy.audit_service.dao.entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IAuditDao extends JpaRepository<AuditEntity, UUID> {

}
