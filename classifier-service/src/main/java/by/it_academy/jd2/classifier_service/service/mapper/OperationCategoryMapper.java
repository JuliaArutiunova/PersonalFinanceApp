package by.it_academy.jd2.classifier_service.service.mapper;

import by.it_academy.jd2.classifier_service.dto.OperationCategoryDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.jd2.classifier_service.storage.entity.OperationCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
@Component
public class OperationCategoryMapper {
    public OperationCategoryDTO mapEntityToDTO(OperationCategoryEntity operationCategoryEntity) {
        return OperationCategoryDTO.builder()
                .uuid(operationCategoryEntity.getId())
                .title(operationCategoryEntity.getTitle())
                .dtCreate(operationCategoryEntity.getDtCreate().toEpochSecond(ZoneOffset.UTC))
                .dtUpdate(operationCategoryEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC))
                .build();
    }

    public PageDTO<OperationCategoryDTO> mapOperationCategoryPage(Page<OperationCategoryEntity> page) {
        PageDTO<OperationCategoryDTO> operationCategoryPageDTO = new PageDTO<>();
        operationCategoryPageDTO.setNumber(page.getNumber());
        operationCategoryPageDTO.setSize(page.getSize());
        operationCategoryPageDTO.setTotalPages(page.getTotalPages());
        operationCategoryPageDTO.setTotalElements(page.getTotalElements());
        operationCategoryPageDTO.setFirst(page.isFirst());
        operationCategoryPageDTO.setLast(page.isLast());
        operationCategoryPageDTO.setNumberOfElements(page.getNumberOfElements());

        List<OperationCategoryDTO> operationCategoryDTOS = new ArrayList<>();
        page.getContent().forEach(operationCategoryEntity -> {
            operationCategoryDTOS.add(mapEntityToDTO(operationCategoryEntity));
        });
        operationCategoryPageDTO.setContent(operationCategoryDTOS);
        return operationCategoryPageDTO;
    }
}
