package by.it_academy.jd2.classifier_service.service.api;

import by.it_academy.jd2.classifier_service.dto.OperationCategoryCreateDTO;
import by.it_academy.jd2.classifier_service.dto.OperationCategoryDTO;
import by.it_academy.lib.dto.PageDTO;

public interface IOperationCategoryService {
    void create(OperationCategoryCreateDTO operationCategoryCreateDTO);

    PageDTO<OperationCategoryDTO> getOperationCategoryPage(int pageNumber, int size);

}
