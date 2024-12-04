package by.it_academy.jd2.service;

import by.it_academy.jd2.dao.api.IOperationDao;
import by.it_academy.jd2.dao.entity.AccountEntity;
import by.it_academy.jd2.dao.entity.CurrencyIdEntity;
import by.it_academy.jd2.dao.entity.OperationCategoryIdEntity;
import by.it_academy.jd2.dao.entity.OperationEntity;
import by.it_academy.jd2.dto.OperationCreateDTO;
import by.it_academy.jd2.dto.OperationDTO;
import by.it_academy.jd2.dto.exchangeRate.RecalculationDTO;
import by.it_academy.jd2.service.api.IAccountService;
import by.it_academy.jd2.service.api.ICurrencyService;
import by.it_academy.jd2.service.api.IOperationCategoryService;
import by.it_academy.jd2.service.api.IOperationService;
import by.it_academy.jd2.service.utils.MoneyOperator;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.dto.PaginationDTO;
import by.it_academy.lib.exception.DataChangedException;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class OperationService implements IOperationService {
    private final IOperationDao operationDao;
    private final IAccountService accountService;
    private final IOperationCategoryService operationCategoryService;
    private final ICurrencyService currencyService;
    private final ModelMapper modelMapper;
    private final MoneyOperator moneyOperator;

    public OperationService(IOperationDao operationDao, IAccountService accountService, IOperationCategoryService operationCategoryService, ICurrencyService currencyService, ModelMapper modelMapper, MoneyOperator moneyOperator) {
        this.operationDao = operationDao;
        this.accountService = accountService;
        this.operationCategoryService = operationCategoryService;
        this.currencyService = currencyService;
        this.modelMapper = modelMapper;
        this.moneyOperator = moneyOperator;
    }

    @Override
    @Transactional
    public void create(UUID accountId, OperationCreateDTO operationCreateDTO) {
        AccountEntity account = accountService.getAccountEntity(accountId);

        CurrencyIdEntity currency = currencyService.get(operationCreateDTO.getCurrency());

        OperationCategoryIdEntity operationCategory = operationCategoryService
                .get(operationCreateDTO.getCategory());

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setId(UUID.randomUUID());
        operationEntity.setAccount(account);
        operationEntity.setDate(Instant.ofEpochSecond(operationCreateDTO.getDate())
                .atZone(ZoneOffset.UTC).toLocalDate());
        operationEntity.setDescription(operationCreateDTO.getDescription());
        operationEntity.setCategory(operationCategory);
        operationEntity.setValue(operationCreateDTO.getValue());
        operationEntity.setCurrency(currency);

        double newBalance = moneyOperator.calculateBalance(account.getBalance(),
                operationCreateDTO.getValue(), account.getCurrency().getId(),
                operationCreateDTO.getCurrency());

        account.setBalance(newBalance);
        accountService.save(account);

        operationDao.saveAndFlush(operationEntity);

    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<OperationDTO> get(UUID accountId, PaginationDTO paginationDTO) {
        Page<OperationEntity> operationEntityPage =
                operationDao.findAllByAccountId(accountId,
                        PageRequest.of(paginationDTO.getPage(), paginationDTO.getSize()));

        if (paginationDTO.getPage() > operationEntityPage.getTotalPages() - 1) {
            throw new PageNotExistException("Страницы с таким номером не существует");
        }

        return modelMapper.map(operationEntityPage, new TypeToken<PageDTO<OperationDTO>>() {
        }.getType());
    }

    @Override
    @Transactional(readOnly = true)
    public OperationEntity get(UUID uuid) {
        return operationDao.findById(uuid).orElseThrow(() ->
                new RecordNotFoundException("Операция не найдена"));
    }


    @Override
    @Transactional
    public void update(UUID accountId, UUID operationId, long dtUpdate, OperationCreateDTO operationCreateDTO) {

        OperationEntity operationEntity = operationDao.findByOperationIdAndAccountId(operationId, accountId);

        if (operationEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new DataChangedException();
        }

        CurrencyIdEntity currency = currencyService.get(operationCreateDTO.getCurrency());

        OperationCategoryIdEntity operationCategory = operationCategoryService
                .get(operationCreateDTO.getCategory());

        operationEntity.setDate(Instant.ofEpochSecond(operationCreateDTO.getDate())
                .atZone(ZoneOffset.UTC).toLocalDate());
        operationEntity.setDescription(operationCreateDTO.getDescription());
        operationEntity.setCategory(operationCategory);
        operationEntity.setValue(operationCreateDTO.getValue());

        if (!operationCreateDTO.getCurrency().equals(operationEntity.getCurrency().getId()) ||
                operationCreateDTO.getValue() != operationEntity.getValue()) {
            AccountEntity account = operationEntity.getAccount();
            double newBalance = moneyOperator.recalculateBalance(
                    RecalculationDTO.builder()
                            .oldValue(operationEntity.getValue())
                            .oldCurrency(operationEntity.getCurrency().getId())
                            .newValue(operationCreateDTO.getValue())
                            .newCurrency(operationCreateDTO.getCurrency())
                            .accountBalance(account.getBalance())
                            .accountCurrency(account.getCurrency().getId())
                            .build());
            operationEntity.setCurrency(currency);
            account.setBalance(newBalance);
            accountService.save(account);
        }

        operationDao.saveAndFlush(operationEntity);

    }

    @Override
    public void delete(UUID accountId, UUID operationId, long dtUpdate) {

        OperationEntity operationEntity = operationDao.findByOperationIdAndAccountId(operationId, accountId);

        if (operationEntity.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new DataChangedException();
        }
        AccountEntity account = operationEntity.getAccount();
        double newBalance = moneyOperator.rollbackBalance(account.getBalance(), operationEntity.getValue(),
                account.getCurrency().getId(), operationEntity.getCurrency().getId());

        account.setBalance(newBalance);

        accountService.save(account);

        operationDao.delete(operationEntity);

    }


}
