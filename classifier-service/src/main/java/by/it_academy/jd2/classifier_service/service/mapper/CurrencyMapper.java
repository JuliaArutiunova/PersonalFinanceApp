package by.it_academy.jd2.classifier_service.service.mapper;

import by.it_academy.jd2.classifier_service.dto.CurrencyDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.jd2.classifier_service.storage.entity.CurrencyEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
@Component
public class CurrencyMapper {
    public CurrencyDTO mapEntityToDTO(CurrencyEntity currencyEntity) {
        return CurrencyDTO.builder()
                .uuid(currencyEntity.getId())
                .title(currencyEntity.getTitle())
                .description(currencyEntity.getDescription())
                .dtCreate(currencyEntity.getDtCreate().toEpochSecond(ZoneOffset.UTC))
                .dtUpdate(currencyEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC))
                .build();

    }

    public PageDTO<CurrencyDTO> mapCurrencyPage(Page<CurrencyEntity> page) {
        PageDTO<CurrencyDTO> currencyPageDTO = new PageDTO<>();

        currencyPageDTO.setNumber(page.getNumber());
        currencyPageDTO.setSize(page.getSize());
        currencyPageDTO.setTotalPages(page.getTotalPages());
        currencyPageDTO.setTotalElements(page.getTotalElements());
        currencyPageDTO.setFirst(page.isFirst());
        currencyPageDTO.setLast(page.isLast());
        currencyPageDTO.setNumberOfElements(page.getNumberOfElements());

        List<CurrencyDTO> currencyDTOS = new ArrayList<>();
        page.getContent().forEach(currencyEntity -> {
            currencyDTOS.add(mapEntityToDTO(currencyEntity));
        });
        currencyPageDTO.setContent(currencyDTOS);
        return currencyPageDTO;
    }
}
