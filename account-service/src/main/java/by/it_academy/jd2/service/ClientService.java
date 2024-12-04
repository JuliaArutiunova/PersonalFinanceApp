package by.it_academy.jd2.service;

import by.it_academy.jd2.dto.exchangeRate.ExchangeRateInfo;
import by.it_academy.jd2.service.api.IClientService;
import by.it_academy.lib.dto.CurrencyNamesDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
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
    public ExchangeRateInfo getExchangeRate(String baseCurrency, String... currency) {

        String currencies = getCurrenciesParamString(currency);

        String url = UriComponentsBuilder.fromHttpUrl(currencyApiUrl)
                .queryParam("apikey", currencyApiKey)
                .queryParam("base_currency", baseCurrency)
                .queryParam("currencies", currencies)
                .build().toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ExchangeRateInfo.class)
                .block();


    }

    @Override
    public Map<UUID, String> getCurrencyNames(UUID... currencies) {

        return webClient.post()
                .uri(classifierUrl).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(currencies))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<UUID, String>>() {
                })
                .block();
    }

    private String getCurrenciesParamString(String[] currency) {
        int currencyLength = currency.length;

        if (currencyLength > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < currencyLength; i++) {
                stringBuilder.append(currency[i]);
                stringBuilder.append(i != currencyLength - 1 ? "," : "");
            }
            return stringBuilder.toString();

        } else {
            return currency[0];
        }
    }


}
