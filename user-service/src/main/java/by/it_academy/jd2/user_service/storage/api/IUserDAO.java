package by.it_academy.jd2.user_service.storage.api;

import by.it_academy.jd2.user_service.storage.projection.UserInfoProjection;
import by.it_academy.jd2.user_service.storage.projection.UserProjection;
import by.it_academy.jd2.user_service.storage.entity.UserEntity;
import by.it_academy.jd2.user_service.storage.projection.UserLoginProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserDAO extends JpaRepository<UserEntity, UUID> {


    Page<UserProjection> findAllProjectedBy(Pageable pageable);

    Optional<UserProjection> findUserProjectionByUserId(UUID userId);

    UserInfoProjection findUserInfoProjectionByUserId(UUID userId);

    List<UserInfoProjection> findAllProjectedByUserIdIn(Iterable<UUID> userIds);

    boolean existsByMail(String mail);


    Optional<UserLoginProjection> findUserLoginProjectionByMail(String mail);


}
