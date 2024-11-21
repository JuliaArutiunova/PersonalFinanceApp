package by.it_academy.jd2.classifier_service.storage.api;

import by.it_academy.jd2.classifier_service.storage.entity.OperationCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOperationCategoryDAO extends JpaRepository<OperationCategoryEntity, UUID> {
    boolean existsByTitle(String title);
}
