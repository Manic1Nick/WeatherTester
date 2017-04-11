package ua.nick.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.weather.model.Diff;
import ua.nick.weather.model.Provider;

import java.util.List;

public interface DiffRepository extends JpaRepository<Diff, Long> {
    Diff findById(Long id);
    List<Diff> findByDate(String date);
    List<Diff> findByProvider(Provider provider);
    Diff findByIdAndProvider(Long id, Provider provider);
    Diff findByDateAndProvider(String date, Provider provider);
}