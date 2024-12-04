package by.it_academy.jd2.service.api;

import by.it_academy.jd2.dto.exchangeRate.ExchangeRateInfo;
import by.it_academy.lib.dto.CurrencyNamesDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IClientService {
    ExchangeRateInfo getExchangeRate(String baseCurrency, String... currency);

    Map<UUID,String> getCurrencyNames(UUID... currencies);


}
