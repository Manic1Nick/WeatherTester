package ua.nick.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.weather.model.AverageDiff;
import ua.nick.weather.model.Provider;

public interface AverageDiffRepository extends JpaRepository<AverageDiff, Long> {
    //AverageDiff findById(Long id);
    AverageDiff findByProvider(Provider provider);
}