package by.it_academy.jd2.classifier_service.service;


import by.it_academy.jd2.classifier_service.dto.OperationCategoryCreateDTO;
import by.it_academy.jd2.classifier_service.dto.OperationCategoryDTO;
import by.it_academy.jd2.classifier_service.service.api.IClientService;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordAlreadyExistException;
import by.it_academy.jd2.classifier_service.service.api.IOperationCategoryService;
import by.it_academy.jd2.classifier_service.storage.api.IOperationCategoryDAO;
import by.it_academy.jd2.classifier_service.storage.entity.OperationCategoryEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
public class OperationCategoryService implements IOperationCategoryService {

    private final IOperationCategoryDAO operationCategoryDAO;

    private final IClientService clientService;

    private final ModelMapper modelMapper;

    public OperationCategoryService(IOperationCategoryDAO operationCategoryDAO, IClientService clientService, ModelMapper modelMapper) {
        this.operationCategoryDAO = operationCategoryDAO;
        this.clientService = clientService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void create(OperationCategoryCreateDTO operationCategoryCreateDTO) {

        if (operationCategoryDAO.existsByTitle(operationCategoryCreateDTO.getTitle())) {
            throw new RecordAlreadyExistException("Категория с таким названием уже существует");
        }

        OperationCategoryEntity operationCategory = OperationCategoryEntity.builder()
                .id(UUID.randomUUID())
                .title(operationCategoryCreateDTO.getTitle())
                .build();
        operationCategoryDAO.saveAndFlush(operationCategory);

        clientService.saveOperationCategoryInAccount(operationCategory.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<OperationCategoryDTO> getOperationCategoryPage(int pageNumber, int size) {
        Page<OperationCategoryEntity> page = operationCategoryDAO.findAll(PageRequest.of(pageNumber, size));

        if (pageNumber > page.getTotalPages() - 1) {
            throw new PageNotExistException("Страницы с таким номером не существует");
        }

        return modelMapper.map(page, new TypeToken<PageDTO<OperationCategoryDTO>>() {
        }.getType());
    }


}
