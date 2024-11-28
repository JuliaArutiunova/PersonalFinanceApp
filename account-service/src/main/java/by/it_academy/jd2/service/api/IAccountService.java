package by.it_academy.jd2.service.api;

import by.it_academy.jd2.dao.entity.AccountEntity;
import by.it_academy.jd2.dto.AccountCreateDTO;
import by.it_academy.jd2.dto.AccountDTO;
import by.it_academy.lib.dto.PageDTO;

import java.util.UUID;

public interface IAccountService {

    void create(AccountCreateDTO accountCreateDTO);

    PageDTO<AccountDTO> getAccountInfo(int page, int size);

    AccountDTO getAccountInfo(UUID id);

    AccountEntity getAccountEntity(UUID id);

    void update(UUID id, AccountCreateDTO accountCreateDTO, long dtUpdate);

    void save(AccountEntity accountEntity);

}
