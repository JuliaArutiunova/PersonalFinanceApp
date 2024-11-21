package by.it_academy.jd2.classifier_service.controller;

import by.it_academy.jd2.classifier_service.dto.*;
import by.it_academy.jd2.classifier_service.service.api.ICurrencyService;
import by.it_academy.jd2.classifier_service.service.api.IOperationCategoryService;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.PaginationDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classifier")
public class ClassifierController {

    private final ICurrencyService currencyService;
    private final IOperationCategoryService operationCategoryService;

    public ClassifierController(ICurrencyService currencyService,
                                IOperationCategoryService operationCategoryService) {
        this.currencyService = currencyService;
        this.operationCategoryService = operationCategoryService;
    }

    @PostMapping("/currency")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCurrency(@Valid @RequestBody CurrencyCreateDTO currencyCreateDTO) {

        currencyService.create(currencyCreateDTO);

    }

    @GetMapping("/currency")
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<CurrencyDTO> getCurrencyPage(@Valid PaginationDTO paginationDTO) {

        return currencyService.getCurrencyPage(paginationDTO.getPage(), paginationDTO.getSize());
    }

    @PostMapping("/operation/category")
    @ResponseStatus(HttpStatus.CREATED)
    public void addOperationCategory(@Valid
                                     @RequestBody OperationCategoryCreateDTO operationCategoryCreateDTO) {
        operationCategoryService.create(operationCategoryCreateDTO);

    }
    @GetMapping("/operation/category")
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<OperationCategoryDTO> getOperationCategoryPage(@Valid PaginationDTO paginationDTO) {

        return operationCategoryService.
                getOperationCategoryPage(paginationDTO.getPage(), paginationDTO.getSize());
    }
}
