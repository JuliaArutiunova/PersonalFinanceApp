package by.it_academy.jd2.user_service.storage.api;

import by.it_academy.jd2.user_service.storage.projection.UserProjection;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.projection.UserLoginProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserStorage extends JpaRepository<UserEntity, UUID> {


    Page<UserProjection> findAllProjectedBy(Pageable pageable);

    Optional<UserProjection> findUserProjectionByUserId(UUID userId);

    boolean existsByMail(String mail);

    Optional<UserEntity> findByMail(String mail);

    Optional<UserLoginProjection> findUserLoginProjectionByMail(String mail);


}
