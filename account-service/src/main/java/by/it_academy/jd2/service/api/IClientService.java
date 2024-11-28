package by.it_academy.jd2.service.api;

import by.it_academy.lib.dto.CurrencyNamesDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IClientService {
    Double getExchangeRate(String baseCurrency, String currency);

    CurrencyNamesDTO getCurrencyNames(UUID operationCurrency, UUID accountCurrency);

}
