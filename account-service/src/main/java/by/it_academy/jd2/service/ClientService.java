package by.it_academy.jd2.service;

import by.it_academy.jd2.dto.exchangeRate.ExchangeRateInfo;
import by.it_academy.jd2.service.api.IClientService;
import by.it_academy.lib.dto.CurrencyNamesDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ClientService implements IClientService {

    private final WebClient webClient;
    @Value("${client.currency-api.key}")
    private String currencyApiKey;
    @Value("${client.currency-api.url}")
    private String currencyApiUrl;
    @Value("${client.classifier-service.url}")
    private String classifierUrl;

    public ClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Double getExchangeRate(String baseCurrency, String currency) {

        String url = UriComponentsBuilder.fromHttpUrl(currencyApiUrl)
                .queryParam("apikey", currencyApiKey)
                .queryParam("base_currency", baseCurrency)
                .queryParam("currencies", currency)
                .build().toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ExchangeRateInfo.class)
                .map(exchangeRateInfo -> exchangeRateInfo.getData().get(currency).getValue()).block();


    }

    @Override
    public CurrencyNamesDTO getCurrencyNames(UUID operationCurrency, UUID accountCurrency) {
        String url = UriComponentsBuilder.fromHttpUrl(classifierUrl)
                .queryParam("operation_currency", operationCurrency)
                .queryParam("account_currency", accountCurrency)
                .build().toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CurrencyNamesDTO.class)
                .block();
    }


}
