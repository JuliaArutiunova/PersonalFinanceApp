package by.it_academy.audit_service.dao.entity;

import by.it_academy.lib.enums.EssenceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "audit", schema = "audit_data")
public class AuditEntity {
    @Id
    @Column(name = "audit_id")
    private UUID auditId;

    @Column(name = "dt_create")
    private LocalDateTime dtCreate;

    @Column(name = "dt_update")
    private LocalDateTime dtUpdate;

    @Column(name = "user_id")
    private UUID user;

    @Column
    private String text;

    @Column(name = "essence_type")
    @Enumerated(EnumType.STRING)
    private EssenceType type;

    @Column(name = "entity_id")
    private UUID entityId;


    @PrePersist
    private void prePersist() {
        this.dtCreate = LocalDateTime.now();
        this.dtUpdate = this.dtCreate;
    }

}
