package ua.nick.weather.service;

import ua.nick.weather.exception.ForecastNotFoundInDBException;
import ua.nick.weather.exception.NoDataFromProviderException;
import ua.nick.weather.model.*;
import ua.nick.weather.modelTester.TesterAverage;
import ua.nick.weather.modelTester.TesterItem;

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
    List<Forecast> getAllNewActuals() throws IOException, URISyntaxException, ParseException;
    Forecast getActualWeatherFromProvider(Provider provider) throws URISyntaxException, IOException, ParseException;
    List<String> getListSeparatedIds(String date) throws ForecastNotFoundInDBException;

    void saveNewDiff(Diff diff);
    List<AverageDiff> getAllAverageDiffs();
    //List<Integer> createListOfAverageItems();
    List<TesterAverage> createListAverageTesters(String date);
    Map<Provider, List<TesterItem>> createMapItemTesters(String ids);
    List<Diff> createListDiffsForPeriod(LocalDate from, LocalDate to);
    Map<Provider, List<Forecast>> createMapProviderForecastsForPeriod(LocalDate from, LocalDate to);
    List<String> createListStringDatesOfPeriod(LocalDate from, LocalDate to);

    //admin
    List<AverageDiff> updateAverageDiffForAllDays();
}
