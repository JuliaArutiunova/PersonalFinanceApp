package by.it_academy.jd2.service;

import by.it_academy.jd2.dao.api.IAccountDao;
import by.it_academy.jd2.dao.entity.AccountEntity;
import by.it_academy.jd2.dao.entity.AccountType;
import by.it_academy.jd2.dao.entity.CurrencyIdEntity;
import by.it_academy.jd2.dto.AccountCreateDTO;
import by.it_academy.jd2.dto.AccountDTO;
import by.it_academy.jd2.service.api.IAccountService;
import by.it_academy.jd2.service.api.IClientService;
import by.it_academy.jd2.service.api.ICurrencyService;
import by.it_academy.jd2.service.utils.MoneyOperator;
import by.it_academy.lib.dto.AuditCreateDTO;
import by.it_academy.lib.dto.PageDTO;
import by.it_academy.lib.enums.EssenceType;
import by.it_academy.lib.exception.DataChangedException;
import by.it_academy.lib.exception.PageNotExistException;
import by.it_academy.lib.exception.RecordNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class AccountService implements IAccountService {

    private final IAccountDao accountDao;
    private final ICurrencyService currencyService;
    private final IClientService clientService;
    private final UserHolder userHolder;
    private final MoneyOperator moneyOperator;
    private final ModelMapper modelMapper;

    public AccountService(IAccountDao accountDao, ICurrencyService currencyService, IClientService clientService,
                          UserHolder userHolder, MoneyOperator moneyOperator, ModelMapper modelMapper) {
        this.accountDao = accountDao;
        this.currencyService = currencyService;
        this.clientService = clientService;
        this.userHolder = userHolder;
        this.moneyOperator = moneyOperator;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void create(AccountCreateDTO accountCreateDTO) {

        CurrencyIdEntity currencyId = currencyService.get(accountCreateDTO.getCurrency());

        UUID userId = userHolder.getUserId();

        AccountEntity account = new AccountEntity();
        account.setId(UUID.randomUUID());
        account.setUser(userId);
        account.setTitle(accountCreateDTO.getTitle());
        account.setDescription(accountCreateDTO.getDescription());
        account.setType(AccountType.valueOf(accountCreateDTO.getType()));
        account.setCurrency(currencyId);

        accountDao.saveAndFlush(account);

        clientService.toAudit(AuditCreateDTO.builder()
                .user(userHolder.getUserId())
                .text("Пользователь создал счёт")
                .entityId(account.getId())
                .essenceType(EssenceType.ACCOUNT)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public PageDTO<AccountDTO> getAccountInfo(int page, int size) {
        UUID userId = userHolder.getUserId();
        Page<AccountEntity> accountEntityPage = accountDao.findAllByUser(userId, PageRequest.of(page, size));

        if (page > accountEntityPage.getTotalPages() - 1) {
            throw new PageNotExistException("Страницы с таким номером не существует");
        }

        return modelMapper.map(accountEntityPage, new TypeToken<PageDTO<AccountDTO>>() {
        }.getType());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDTO getAccountInfo(UUID id) {

        AccountEntity accountEntity = getAccountEntity(id);

        return modelMapper.map(accountEntity, AccountDTO.class);

    }

    @Override
    @Transactional(readOnly = true)
    public AccountEntity getAccountEntity(UUID id) {
        return accountDao.findById(id).orElseThrow(() ->
                new RecordNotFoundException("Счет не найден"));
    }


    @Override
    @Transactional
    public void update(UUID id, AccountCreateDTO accountCreateDTO, long dtUpdate) {
        AccountEntity account = getAccountEntity(id);

        if (account.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new DataChangedException();
        }

        if (!account.getCurrency().getId().equals(accountCreateDTO.getCurrency()) &&
                account.getBalance() != 0) {

            CurrencyIdEntity currencyId = currencyService.get(accountCreateDTO.getCurrency());

            account.setCurrency(currencyId);

            double convertedBalance = moneyOperator.convertBalance(account.getBalance(),
                    accountCreateDTO.getCurrency(), accountCreateDTO.getCurrency());

            account.setBalance(convertedBalance);
        }

        account.setTitle(accountCreateDTO.getTitle());
        account.setDescription(accountCreateDTO.getDescription());
        account.setType(AccountType.valueOf(accountCreateDTO.getType()));

        accountDao.saveAndFlush(account);
    }

    @Override
    @Transactional
    public void save(AccountEntity accountEntity) {
        accountDao.saveAndFlush(accountEntity);
    }


}
