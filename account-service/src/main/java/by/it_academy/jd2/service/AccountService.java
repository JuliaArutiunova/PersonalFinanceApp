package by.it_academy.jd2.service;

import by.it_academy.jd2.dao.api.IAccountDao;
import by.it_academy.jd2.dao.entity.AccountEntity;
import by.it_academy.jd2.dao.entity.AccountType;
import by.it_academy.jd2.dao.entity.CurrencyIdEntity;
import by.it_academy.jd2.dao.entity.UserIdEntity;
import by.it_academy.jd2.dto.AccountCreateDTO;
import by.it_academy.jd2.dto.AccountDTO;
import by.it_academy.jd2.service.api.IAccountService;
import by.it_academy.jd2.service.api.ICurrencyService;
import by.it_academy.jd2.service.api.IUserService;
import by.it_academy.jd2.service.utils.MoneyOperator;
import by.it_academy.lib.dto.PageDTO;
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
    private final IUserService userService;
    private final ICurrencyService currencyService;
    private final UserHolder userHolder;
    private final MoneyOperator moneyOperator;
    private final ModelMapper modelMapper;

    public AccountService(IAccountDao accountDao,
                          IUserService userService, ICurrencyService currencyService, UserHolder userHolder, MoneyOperator moneyOperator, ModelMapper modelMapper) {
        this.accountDao = accountDao;
        this.userService = userService;
        this.currencyService = currencyService;
        this.userHolder = userHolder;
        this.moneyOperator = moneyOperator;

        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void create(AccountCreateDTO accountCreateDTO) {
        UUID userId = userHolder.getUserId();
        UserIdEntity userIdEntity = userService.getUserIdEntity(userId);

        CurrencyIdEntity currencyId = currencyService.get(accountCreateDTO.getCurrency());

        AccountEntity account = new AccountEntity();
        account.setId(UUID.randomUUID());
        account.setUser(userIdEntity);
        account.setTitle(accountCreateDTO.getTitle());
        account.setDescription(accountCreateDTO.getDescription());
        account.setType(AccountType.valueOf(accountCreateDTO.getType()));
        account.setCurrency(currencyId);

        accountDao.saveAndFlush(account);
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

        CurrencyIdEntity currencyId = currencyService.get(accountCreateDTO.getCurrency());

        if (account.getDtUpdate().toEpochSecond(ZoneOffset.UTC) != dtUpdate) {
            throw new DataChangedException();
        }

        if (!currencyId.getId().equals(accountCreateDTO.getCurrency()) &&
                account.getBalance() != 0) {
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
