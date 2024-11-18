package by.it_academy.jd2.user_service.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

@Entity

@Table(name = "user", schema = "user_data")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column
    private String fio;

    @Column
    private String mail;

    @Column
    private String password;

    @Column(name = "dt_create")
    private Timestamp dtCreate;

    @Version
    @Column(name = "dt_update")
    private Timestamp dtUpdate;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(columnDefinition = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "code")
    private String activationCode;
}
