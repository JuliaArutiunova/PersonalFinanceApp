package by.it_academy.lib.dto;

import by.it_academy.lib.enums.EssenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class AuditCreateDTO {
    private UUID user;
    private String text;
    private EssenceType essenceType;
    private UUID entityId;


}
