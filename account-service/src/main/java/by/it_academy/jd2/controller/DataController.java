package by.it_academy.jd2.controller;

import by.it_academy.jd2.service.api.ICurrencyService;
import by.it_academy.jd2.service.api.IOperationCategoryService;
import by.it_academy.jd2.service.api.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/account_data")
public class DataController {

    private final IUserService userService;
    private final ICurrencyService currencyService;
    private final IOperationCategoryService operationCategoryService;

    public DataController(IUserService userService, ICurrencyService currencyService, IOperationCategoryService operationCategoryService) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.operationCategoryService = operationCategoryService;
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUser(@RequestParam("user_id") UUID userId) {
        userService.save(userId);
    }

    @PostMapping("/currency")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCurrency(@RequestParam("currency_id") UUID currencyId){
        currencyService.save(currencyId);
    }

    @PostMapping("/operation_category")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveOperationCategory(@RequestParam("operation_category_id") UUID currencyId){
        operationCategoryService.save(currencyId);
    }
}
