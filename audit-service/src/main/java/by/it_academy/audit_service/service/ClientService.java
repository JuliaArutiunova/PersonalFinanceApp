package by.it_academy.audit_service.service;

import by.it_academy.audit_service.service.api.IClientService;
import by.it_academy.lib.dto.UserInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ClientService implements IClientService {

    private final WebClient webClient;
    @Value("${client.user-service.url}")
    private String userServiceUrl;

    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public UserInfoDTO getUserInfo(UUID userId) {
        String url = UriComponentsBuilder.fromHttpUrl(userServiceUrl)
                .queryParam("user_id", userId)
                .build().toUriString();
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(UserInfoDTO.class)
                .block();
    }

    @Override
    public Map<UUID, UserInfoDTO> getUsersInfo(Set<UUID> users) {
        return webClient.post()
                .uri(userServiceUrl).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(users))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<UUID, UserInfoDTO>>() {
                })
                .block();
    }


}
