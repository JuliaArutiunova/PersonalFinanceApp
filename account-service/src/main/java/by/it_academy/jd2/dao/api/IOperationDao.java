package by.it_academy.jd2.dao.api;

import by.it_academy.jd2.dao.entity.OperationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOperationDao extends JpaRepository<OperationEntity, UUID> {
    Page<OperationEntity> findAllByAccountId(UUID id, Pageable pageable);
}
