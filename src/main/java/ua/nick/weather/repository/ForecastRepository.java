package ua.nick.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.model.Provider;

import java.util.List;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {
    Forecast findById(Long id);
    List<Forecast> findByDate(String date);
    //List<Forecast> findByProvider(Provider provider);
    //List<Forecast> findByProviderAndActual(Provider provider, boolean actual);
    //Forecast findByIdAndProvider(Long id, Provider provider);
    //List<Forecast> findByDateAndProvider(String date, Provider provider);
    //List<Forecast> findByDateAndActual(String date, boolean actual);
    Forecast findByDateAndProviderAndActual(String date, Provider provider, boolean actual);
    //List<Forecast> findByDateAndProviderAndActual(String date, Provider provider, boolean actual, Pageable pageable);
}