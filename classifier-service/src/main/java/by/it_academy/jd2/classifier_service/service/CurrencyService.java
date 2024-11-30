package by.it_academy.jd2.classifier_service.service;

import by.it_academy.jd2.classifier_service.dto.CurrencyCreateDTO;
import by.it_academy.jd2.classifier_service.dto.CurrencyDTO;
import by.it_academy.lib.dto.CurrencyNamesDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordAlreadyExistException;
import by.it_academy.jd2.classifier_service.service.api.ICurrencyService;
import by.it_academy.jd2.classifier_service.storage.api.ICurrencyDAO;
import by.it_academy.jd2.classifier_service.storage.entity.CurrencyEntity;
import by.it_academy.lib.exception.RecordNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CurrencyService implements ICurrencyService {

    private final ICurrencyDAO currencyDAO;

    private final ModelMapper modelMapper;

    private final ClientService clientService;


    public CurrencyService(ICurrencyDAO currencyDAO, ModelMapper modelMapper, ClientService clientService) {
        this.currencyDAO = currencyDAO;
        this.modelMapper = modelMapper;
        this.clientService = clientService;
    }

    @Override
    @Transactional
    public void create(CurrencyCreateDTO currencyCreateDTO) {
        String title = currencyCreateDTO.getTitle();

        if (currencyDAO.existsByTitle(title)) {
            throw new RecordAlreadyExistException("Валюта с названием " + title + " уже существует");
        }
        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .id(UUID.randomUUID())
                .title(title)
                .description(currencyCreateDTO.getDescription())
                .build();
        currencyDAO.saveAndFlush(currencyEntity);

        clientService.saveCurrencyInAccount(currencyEntity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<CurrencyDTO> getCurrencyPage(int pageNumber, int size) {

        Page<CurrencyEntity> page = currencyDAO.findAll(PageRequest.of(pageNumber, size));

        if (pageNumber > page.getTotalPages() - 1) {
            throw new PageNotExistException("Страницы с таким номером не существует");
        }
        return modelMapper.map(page, new TypeToken<PageDTO<CurrencyDTO>>() {
        }.getType());
    }


    @Override
    public CurrencyNamesDTO getNames(UUID operationCurrency, UUID accountCurrency) {
        String operationCurrencyName = get(operationCurrency).getTitle();
        String accountCurrencyName = get(accountCurrency).getTitle();
        return new CurrencyNamesDTO(operationCurrencyName, accountCurrencyName);
    }

    @Override
    public CurrencyEntity get(UUID id) {
        return currencyDAO.findById(id).orElseThrow(() ->
                new RecordNotFoundException("Валюта не найдена"));
    }


}
