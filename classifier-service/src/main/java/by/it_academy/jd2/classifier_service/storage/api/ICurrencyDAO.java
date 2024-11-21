package by.it_academy.jd2.classifier_service.storage.api;

import by.it_academy.jd2.classifier_service.storage.entity.CurrencyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICurrencyDAO extends JpaRepository<CurrencyEntity, UUID> {
    boolean existsByTitle(String title);
}
