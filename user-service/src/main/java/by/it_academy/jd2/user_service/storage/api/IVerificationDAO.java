package by.it_academy.jd2.user_service.storage.api;

import by.it_academy.jd2.user_service.storage.entity.VerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface IVerificationDAO extends JpaRepository<VerificationEntity, UUID> {
    @Query("SELECT v FROM VerificationEntity v JOIN v.userEntity u WHERE u.mail = :mail")
    Optional<VerificationEntity> findVerificationByUserMail(@Param("mail") String mail);
}
