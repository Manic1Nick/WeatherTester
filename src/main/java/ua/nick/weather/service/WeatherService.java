package ua.nick.weather.service;

import ua.nick.weather.exception.ForecastNotFoundInDBException;
import ua.nick.weather.exception.NoDataFromProviderException;
import ua.nick.weather.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface WeatherService {

    void saveNewForecast(Forecast forecast);
    Forecast getForecastById(Long id);
    List<List<Forecast>> getAllNewForecasts() throws IOException, URISyntaxException, ParseException, NoDataFromProviderException;
    List<Forecast> getNewForecastsFromProvider(Provider provider) throws URISyntaxException, IOException, ParseException, NoDataFromProviderException;
    List<Forecast> getAllNewActuals() throws IOException, URISyntaxException, ParseException;
    Forecast getActualWeatherFromProvider(Provider provider) throws URISyntaxException, IOException, ParseException;
    List<Forecast> getAllForecastsFromProvider(Provider provider);
    List<Forecast> getAllForecastsFromProviderAndActual(Provider provider, boolean actual);
    Map<Provider, List<Forecast>> createMapForecastsByProviders(boolean actual);

    //Map<String, Map<String, String>> createDayMapForecastsByItems(String date) throws ForecastNotFoundInDBException;
    //Forecast getForecastFromDBByDateProviderActual(String date, Provider provider, boolean actual) throws ForecastNotFoundInDBException;

    List<List<Long>> getListForecastsAndActualsIds(String date) throws ForecastNotFoundInDBException;
    Map<String, Map<Tester, String>> createDayMapForecastsByIds(String separatedByCommaIds);
    Map<Provider, Map<String, Map<Tester, String>>> createDayMapForecastsByProviders(String separatedByCommaIds);

    void saveNewDiff(Diff diff);
    Diff createAndSaveNewDiff(Forecast forecast, Forecast actual);
    Diff getDiffByDateAndProvider(String date, Provider provider);
    void saveNewAverageDiff(AverageDiff diff);
    AverageDiff getAverageDiff(Provider provider);
    List<AverageDiff> getAllAverageDiffs();
    List<Integer> createListOfAverageItems();

    List<TesterAverage> createListAverageTesters(String date);
    Map<Provider, List<TesterItem>> createMapItemTesters(String ids);
    List<Diff> createListDiffsForPeriod(LocalDate from, LocalDate to);

    //admin
    List<AverageDiff> updateAverageDiffForAllDays();
}
