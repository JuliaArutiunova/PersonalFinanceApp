package by.it_academy.jd2.classifier_service.service.api;

import by.it_academy.lib.dto.AuditCreateDTO;


import java.util.UUID;

public interface IClientService {

    void saveCurrencyInAccount(UUID currencyId);

    void saveOperationCategoryInAccount(UUID operationCategoryId);

    void toAudit(AuditCreateDTO auditCreateDTO);


}
