package by.it_academy.jd2.service.utils;

import by.it_academy.jd2.dto.exchangeRate.ExchangeRateInfo;
import by.it_academy.jd2.dto.RecalculationDTO;
import by.it_academy.jd2.service.api.IClientService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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

        BigDecimal convertedValue = convertValue(value, currency, accountCurrency);

        return convertedValue.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }


    public double recalculateBalance(RecalculationDTO recalculationDTO) {
        BigDecimal balance = BigDecimal.valueOf(recalculationDTO.getAccountBalance());
        BigDecimal oldOperationValue = BigDecimal.valueOf(recalculationDTO.getOldValue());
        BigDecimal newOperationValue = BigDecimal.valueOf(recalculationDTO.getNewValue());

        UUID oldCurrencyId = recalculationDTO.getOldCurrency();
        UUID newCurrencyId = recalculationDTO.getNewCurrency();
        UUID accountCurrencyId = recalculationDTO.getAccountCurrency();

        if (oldCurrencyId.equals(newCurrencyId) && oldCurrencyId.equals(accountCurrencyId)) {

            return balance
                    .add(newOperationValue.subtract(oldOperationValue))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();

        } else if (!oldCurrencyId.equals(newCurrencyId) && !oldCurrencyId.equals(accountCurrencyId)) {

            Map<UUID, String> currencyInfo =
                    clientService.getCurrencyNames(oldCurrencyId, accountCurrencyId, newCurrencyId);

            ExchangeRateInfo exchangeRateInfo =
                    clientService.getExchangeRate(currencyInfo.get(oldCurrencyId),
                            currencyInfo.get(accountCurrencyId), currencyInfo.get(newCurrencyId));

            BigDecimal oldCurrencyExchangeRate =
                    BigDecimal.valueOf(exchangeRateInfo.getData().get(currencyInfo.get(accountCurrencyId)).getValue());

            double newToOldRate = exchangeRateInfo.getData().get(currencyInfo.get(newCurrencyId)).getValue();

            BigDecimal newCurrencyExchangeRate =
                    oldCurrencyExchangeRate.divide(BigDecimal.valueOf(newToOldRate), 10, RoundingMode.HALF_UP);

            return balance
                    .add((newOperationValue.multiply(newCurrencyExchangeRate))
                            .subtract(oldOperationValue.multiply(oldCurrencyExchangeRate)))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();

        } else {

            Map<UUID, String> currencyInfo = clientService.getCurrencyNames(accountCurrencyId, newCurrencyId);

            ExchangeRateInfo exchangeRateInfo =
                    clientService.getExchangeRate(currencyInfo.get(newCurrencyId), currencyInfo.get(accountCurrencyId));

            BigDecimal exchangeRate =
                    BigDecimal.valueOf(exchangeRateInfo.getData().get(currencyInfo.get(accountCurrencyId)).getValue());

            return balance
                    .add((newOperationValue.multiply(exchangeRate))
                            .subtract(oldOperationValue.multiply(exchangeRate)))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();

        }

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

        Map<UUID, String> currencyInfo = clientService.getCurrencyNames(currency,accountCurrency);

        ExchangeRateInfo exchangeRateInfo =
                clientService.getExchangeRate(currencyInfo.get(currency), currencyInfo.get(accountCurrency));

        double exchangeRate
                = exchangeRateInfo.getData().get(currencyInfo.get(accountCurrency)).getValue();

        return BigDecimal.valueOf(value).multiply(BigDecimal.valueOf(exchangeRate));
    }

}
