package by.it_academy.jd2.classifier_service.service;

import by.it_academy.jd2.classifier_service.dto.CurrencyCreateDTO;
import by.it_academy.jd2.classifier_service.dto.CurrencyDTO;
import by.it_academy.lib.dto.AuditCreateDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.enums.EssenceType;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordAlreadyExistException;
import by.it_academy.jd2.classifier_service.service.api.ICurrencyService;
import by.it_academy.jd2.classifier_service.storage.api.ICurrencyDAO;
import by.it_academy.jd2.classifier_service.storage.entity.CurrencyEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CurrencyService implements ICurrencyService {

    private final ICurrencyDAO currencyDAO;

    private final ClientService clientService;

    private final UserHolder userHolder;

    private final ModelMapper modelMapper;

    public CurrencyService(ICurrencyDAO currencyDAO, ClientService clientService, UserHolder userHolder, ModelMapper modelMapper) {
        this.currencyDAO = currencyDAO;
        this.clientService = clientService;
        this.userHolder = userHolder;
        this.modelMapper = modelMapper;
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

        clientService.toAudit(AuditCreateDTO.builder()
                .user(userHolder.getUserId())
                .text("Создана новая валюта")
                .entityId(currencyEntity.getId())
                .essenceType(EssenceType.CURRENCY)
                .build());

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
    public Map<UUID, String> getNames(UUID[] uuids) {
        List<CurrencyEntity> currencyEntities = currencyDAO.findAllById(Arrays.asList(uuids));
        Map<UUID, String> names = new HashMap<>();
        currencyEntities.forEach(currencyEntity -> {
            names.put(currencyEntity.getId(), currencyEntity.getTitle());
        });
        return names;
    }


}
