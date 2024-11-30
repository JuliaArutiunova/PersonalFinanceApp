package by.it_academy.jd2.service.utils;

import by.it_academy.jd2.service.api.IClientService;
import by.it_academy.lib.dto.CurrencyNamesDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
public class MoneyOperator {

    private final IClientService clientService;

    public MoneyOperator(IClientService clientService) {
        this.clientService = clientService;
    }

    public double calculateBalance(double balance, double value,
                                   UUID accountCurrency, UUID operationCurrency) {


        if (accountCurrency.equals(operationCurrency)) {
            return BigDecimal.valueOf(balance).add(BigDecimal.valueOf(value))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
        } else {

            BigDecimal convertedValue = convertValue(value, operationCurrency, accountCurrency);

            return BigDecimal.valueOf(balance).add(convertedValue)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();

        }

    }

    public double convertBalance(double value, UUID currency, UUID accountCurrency) {
        CurrencyNamesDTO currencyNames =
                clientService.getCurrencyNames(currency, accountCurrency);
        Double exchangeRate =
                clientService.getExchangeRate(currencyNames.getCurrencyName(),
                        currencyNames.getAccountCurrencyName());

        return BigDecimal.valueOf(value).multiply(BigDecimal.valueOf(exchangeRate))
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public double rollbackBalance(double balance, double value,
                                  UUID accountCurrency, UUID operationCurrency) {
        if (accountCurrency.equals(operationCurrency)) {
            return BigDecimal.valueOf(balance).subtract(BigDecimal.valueOf(value))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
        } else {

            BigDecimal convertedValue = convertValue(value, operationCurrency, accountCurrency);

            return BigDecimal.valueOf(balance).subtract(convertedValue)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();

        }
    }

    public BigDecimal convertValue(double value, UUID currency, UUID accountCurrency) {
        CurrencyNamesDTO currencyNames =
                clientService.getCurrencyNames(currency, accountCurrency);
        Double exchangeRate =
                clientService.getExchangeRate(currencyNames.getCurrencyName(),
                        currencyNames.getAccountCurrencyName());
        return BigDecimal.valueOf(value).multiply(BigDecimal.valueOf(exchangeRate));
    }

}
