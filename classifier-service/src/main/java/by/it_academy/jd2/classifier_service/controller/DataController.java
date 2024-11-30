package by.it_academy.jd2.classifier_service.controller;

import by.it_academy.jd2.classifier_service.service.api.ICurrencyService;
import by.it_academy.lib.dto.CurrencyNamesDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/classifier_data")
public class DataController {
    private final ICurrencyService currencyService;

    public DataController(ICurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currency")
    @ResponseStatus(HttpStatus.OK)
    public CurrencyNamesDTO getCurrencyNames(@RequestParam("currency") UUID operationCurrency,
                                             @RequestParam("account_currency") UUID accountCurrency) {
        return currencyService.getNames(operationCurrency, accountCurrency);
    }
}
