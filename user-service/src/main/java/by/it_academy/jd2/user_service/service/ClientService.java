package by.it_academy.jd2.user_service.service;

import by.it_academy.jd2.user_service.service.api.IClientService;
import by.it_academy.lib.dto.AuditCreateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
@Service
public class ClientService implements IClientService {
    private final WebClient webClient;
    @Value("${client.audit-service.url}")
    private String auditUrl;

    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void toAudit(AuditCreateDTO auditCreateDTO) {
        webClient.post()
                .uri(auditUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(auditCreateDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();

    }
}
