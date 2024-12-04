package by.it_academy.jd2.classifier_service.controller;

import by.it_academy.jd2.classifier_service.service.api.ICurrencyService;
import by.it_academy.lib.dto.CurrencyNamesDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/classifier_data")
public class DataController {
    private final ICurrencyService currencyService;

    public DataController(ICurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/currency")
    @ResponseStatus(HttpStatus.OK)
    public Map<UUID, String> getCurrencyNames(@RequestBody UUID[] uuids) {

        return currencyService.getNames(uuids); //этот метод возвращает Map<UUID, String>
    }
}
