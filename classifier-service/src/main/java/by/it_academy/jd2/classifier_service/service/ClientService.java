package by.it_academy.jd2.classifier_service.service;

import by.it_academy.jd2.classifier_service.service.api.IClientService;
import by.it_academy.lib.dto.AuditDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
public class ClientService implements IClientService {
    @Value("${client.account-service.currency.url}")
    private String accountCurrencyUrl;
    @Value("${client.account-service.operation-category.url}")
    private String accountOperationCategoryUrl;


    private final WebClient webClient;

    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public void saveCurrencyInAccount(UUID currencyId) {
        String url = UriComponentsBuilder.fromHttpUrl(accountCurrencyUrl)
                .queryParam("currency_id", currencyId)
                .build().toUriString();

        webClient.post()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public void saveOperationCategoryInAccount(UUID operationCategoryId) {
        String url = UriComponentsBuilder.fromHttpUrl(accountOperationCategoryUrl)
                .queryParam("operation_category_id", operationCategoryId)
                .build().toUriString();

        webClient.post()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class).block();
    }

}
