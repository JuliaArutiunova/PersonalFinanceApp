package by.it_academy.jd2.controller;


import by.it_academy.jd2.dto.AccountCreateDTO;
import by.it_academy.jd2.dto.AccountDTO;
import by.it_academy.jd2.dto.OperationCreateDTO;
import by.it_academy.jd2.dto.OperationDTO;
import by.it_academy.jd2.service.api.IAccountService;
import by.it_academy.jd2.service.api.IOperationService;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.PaginationDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final IAccountService accountService;
    private final IOperationService operationService;

    public AccountController(IAccountService accountService, IOperationService operationService) {
        this.accountService = accountService;
        this.operationService = operationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@Valid @RequestBody AccountCreateDTO accountCreateDTO) {
        accountService.create(accountCreateDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<AccountDTO> getAccountPage(@Valid PaginationDTO paginationDTO) {
        return accountService.getAccountInfo(paginationDTO.getPage(), paginationDTO.getSize());
    }

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDTO getAccountInfo(@PathVariable("uuid") UUID uuid) {
        return accountService.getAccountInfo(uuid);
    }


    @PutMapping("/{uuid}/dt_update/{dt_update}")
    @ResponseStatus(HttpStatus.OK)
    public void updateAccount(@PathVariable("uuid") UUID uuid,
                              @PathVariable("dt_update") long dtUpdate,
                              @Valid @RequestBody AccountCreateDTO accountCreateDTO) {

        accountService.update(uuid, accountCreateDTO, dtUpdate);
    }

    @PostMapping("/{uuid}/operation")
    @ResponseStatus(HttpStatus.CREATED)
    public void addOperation(@PathVariable("uuid") UUID accountId,
                             @Valid @RequestBody OperationCreateDTO operationCreateDTO) {

        operationService.create(accountId, operationCreateDTO);
    }

    @GetMapping("/{uuid}/operation")
    @ResponseStatus(HttpStatus.OK)
    public PageDTO<OperationDTO> getOperationPage(@PathVariable("uuid") UUID accountId,
                                                  @Valid PaginationDTO paginationDTO) {
        return operationService.get(accountId, paginationDTO);
    }

    @PutMapping("/{uuid}/operation/{uuid_operation}/dt_update/{dt_update}")
    @ResponseStatus(HttpStatus.OK)
    public void updateOperation(@PathVariable("uuid") UUID accountId,
                                @PathVariable("uuid_operation") UUID operationId,
                                @PathVariable("dt_update") long dtUpdate,
                                @Valid @RequestBody OperationCreateDTO operationCreateDTO) {

        operationService.update(accountId, operationId, dtUpdate, operationCreateDTO);

    }

    @DeleteMapping("/{uuid}/operation/{uuid_operation}/dt_update/{dt_update}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOperation(@PathVariable("uuid") UUID accountId,
                                @PathVariable("uuid_operation") UUID operationId,
                                @PathVariable("dt_update") long dtUpdate) {

        operationService.delete(accountId, operationId, dtUpdate);
    }

}
