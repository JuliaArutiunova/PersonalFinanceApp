package by.it_academy.jd2.classifier_service.service;

import by.it_academy.jd2.classifier_service.dto.OperationCategoryCreateDTO;
import by.it_academy.jd2.classifier_service.dto.OperationCategoryDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordAlreadyExistException;
import by.it_academy.jd2.classifier_service.service.api.IOperationCategoryService;
import by.it_academy.jd2.classifier_service.service.mapper.OperationCategoryMapper;
import by.it_academy.jd2.classifier_service.storage.api.IOperationCategoryDAO;
import by.it_academy.jd2.classifier_service.storage.entity.OperationCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OperationCategoryService implements IOperationCategoryService {

    private final IOperationCategoryDAO operationCategoryDAO;
    private final OperationCategoryMapper operationCategoryMapper;

    public OperationCategoryService(IOperationCategoryDAO operationCategoryDAO,
                                    OperationCategoryMapper operationCategoryMapper) {
        this.operationCategoryDAO = operationCategoryDAO;
        this.operationCategoryMapper = operationCategoryMapper;
    }

    @Override
    @Transactional
    public void create(OperationCategoryCreateDTO operationCategoryCreateDTO) {

        if (operationCategoryDAO.existsByTitle(operationCategoryCreateDTO.getTitle())) {
            throw new RecordAlreadyExistException("Категория с таким названием уже существует");
        }
        operationCategoryDAO.saveAndFlush(OperationCategoryEntity.builder()
                .id(UUID.randomUUID())
                .title(operationCategoryCreateDTO.getTitle())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<OperationCategoryDTO> getOperationCategoryPage(int pageNumber, int size) {
        Page<OperationCategoryEntity> page = operationCategoryDAO.findAll(PageRequest.of(pageNumber, size));

        if (pageNumber > page.getTotalPages() - 1) {
            throw new PageNotExistException("Страницы с таким номером не существует");
        }

        return operationCategoryMapper.mapOperationCategoryPage(page);
    }


}
