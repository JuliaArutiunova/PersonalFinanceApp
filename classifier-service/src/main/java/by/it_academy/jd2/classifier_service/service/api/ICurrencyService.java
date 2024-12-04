package by.it_academy.jd2.classifier_service.service.api;

import by.it_academy.jd2.classifier_service.dto.CurrencyCreateDTO;
import by.it_academy.jd2.classifier_service.dto.CurrencyDTO;
import by.it_academy.jd2.classifier_service.storage.entity.CurrencyEntity;
import by.it_academy.lib.dto.CurrencyNamesDTO;
import by.it_academy.lib.dto.PageDTO;

import java.util.Map;
import java.util.UUID;

public interface ICurrencyService {
    void create(CurrencyCreateDTO currencyCreateDTO);

    PageDTO<CurrencyDTO> getCurrencyPage(int pageNumber, int size);

    Map<UUID, String> getNames(UUID[] uuids);

}
