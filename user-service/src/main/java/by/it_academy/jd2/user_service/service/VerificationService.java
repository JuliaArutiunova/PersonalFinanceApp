package by.it_academy.jd2.user_service.service;

import by.it_academy.lib.exception.RecordNotFoundException;
import by.it_academy.jd2.user_service.service.api.IVerificationService;
import by.it_academy.jd2.user_service.service.utils.Generator;
import by.it_academy.jd2.user_service.storage.api.IVerificationDAO;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.entity.VerificationEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VerificationService implements IVerificationService {

    private final Generator generator;

    private final MailSenderService mailSenderService;

    private final IVerificationDAO verificationDAO;

    public VerificationService(Generator generator, MailSenderService mailSenderService, IVerificationDAO verificationDAO) {
        this.generator = generator;
        this.mailSenderService = mailSenderService;
        this.verificationDAO = verificationDAO;
    }

    @Override
    @Transactional //проверки
    public void create(UserEntity userEntity) {
        String code = generator.generateCode(10);

        verificationDAO.saveAndFlush(VerificationEntity.builder()
                .verificationId(UUID.randomUUID())
                .code(code)
                .userEntity(userEntity)
                .build());

        mailSenderService.sendMail(userEntity.getMail(),
                generator.generateMessageText(userEntity.getFio(), userEntity.getMail(), code));
        
    }

    @Override
    @Transactional
    public VerificationEntity get(String mail) {
        return verificationDAO.findVerificationByUserMail(mail).orElseThrow(() ->
                new UserNotFoundException("Пользователь с email " + mail + " не был найден"));
    }

    @Override
    @Transactional
    public void delete(VerificationEntity verification){
        verificationDAO.delete(verification);
    }
}
