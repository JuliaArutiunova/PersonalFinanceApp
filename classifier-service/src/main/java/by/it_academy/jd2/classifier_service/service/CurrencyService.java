package by.it_academy.jd2.classifier_service.service;

import by.it_academy.jd2.classifier_service.dto.CurrencyCreateDTO;
import by.it_academy.jd2.classifier_service.dto.CurrencyDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordAlreadyExistException;
import by.it_academy.jd2.classifier_service.service.api.ICurrencyService;
import by.it_academy.jd2.classifier_service.service.mapper.CurrencyMapper;
import by.it_academy.jd2.classifier_service.storage.api.ICurrencyDAO;
import by.it_academy.jd2.classifier_service.storage.entity.CurrencyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CurrencyService implements ICurrencyService {

    private final ICurrencyDAO currencyDAO;
    private final CurrencyMapper currencyMapper;


    public CurrencyService(ICurrencyDAO currencyDAO, CurrencyMapper currencyMapper) {
        this.currencyDAO = currencyDAO;
        this.currencyMapper = currencyMapper;
    }

    @Override
    @Transactional
    public void create(CurrencyCreateDTO currencyCreateDTO) {
        String title = currencyCreateDTO.getTitle();

        if (currencyDAO.existsByTitle(title)) {
            throw new RecordAlreadyExistException("Валюта с названием " + title + " уже существует");
        }

        currencyDAO.saveAndFlush(CurrencyEntity.builder()
                .id(UUID.randomUUID())
                .title(title)
                .description(currencyCreateDTO.getDescription())
                .build());

    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<CurrencyDTO> getCurrencyPage(int pageNumber, int size) {

        Page<CurrencyEntity> page = currencyDAO.findAll(PageRequest.of(pageNumber, size));

        if (pageNumber > page.getTotalPages() - 1) {
            throw new PageNotExistException("Страницы с таким номером не существует");
        }

        return currencyMapper.mapCurrencyPage(page);
    }


}
