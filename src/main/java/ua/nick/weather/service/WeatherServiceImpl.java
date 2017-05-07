package ua.nick.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nick.weather.exception.ForecastNotFoundInDBException;
import ua.nick.weather.exception.NoDataFromProviderException;
import ua.nick.weather.model.*;
import ua.nick.weather.modelTester.TesterAverage;
import ua.nick.weather.modelTester.TesterItem;
import ua.nick.weather.repository.AverageDiffRepository;
import ua.nick.weather.repository.DiffRepository;
import ua.nick.weather.repository.ForecastRepository;
import ua.nick.weather.utils.NetUtils;
import ua.nick.weather.weatherFactory.ActualWeatherFactory;
import ua.nick.weather.weatherFactory.ForecastFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service(value = "service")
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private ForecastRepository forecastRepository;
    @Autowired
    private DiffRepository diffRepository;
    @Autowired
    private AverageDiffRepository averageDiffRepository;
    @Autowired
    private ForecastFactory forecastFactory;
    @Autowired
    private ActualWeatherFactory actualFactory;

    @Override
    public void saveNewForecast(Forecast forecast) {
        forecastRepository.save(forecast);
    }

    @Override
    public Forecast getForecastById(Long id) {
        return forecastRepository.findById(id);
    }

    @Override
    public List<List<Forecast>> getAllNewForecasts()
            throws IOException, URISyntaxException, ParseException, NoDataFromProviderException {
        List<List<Forecast>> allForecasts = new ArrayList<>();

        for (Provider provider : Provider.values())
            if (needUpdateForecasts(provider))
                allForecasts.add(createListForecastsFromProvider(provider));

        return allForecasts;
    }

    @Override
    public List<Forecast> getAllNewActuals()
            throws IOException, URISyntaxException, ParseException {
        List<Forecast> actuals = new ArrayList<>();

        for (Provider provider : Provider.values()) {
            if (needUpdateActuals(provider)) {
                Forecast actual = getActualWeatherFromProvider(provider);

                actuals.add(actual);
                saveNewForecast(actual);

                createAndSaveNewDiff(actual);
            }
        }
        return actuals;
    }

    @Override
    public Forecast getActualWeatherFromProvider(Provider provider)
            throws URISyntaxException, IOException, ParseException {

        URI uri = new URI(provider.getLinkActual());
        URL url = uri.toURL();

        String json = NetUtils.urlToString(url);

        return actualFactory.createActualModelFromJson(provider, json);
    }

    @Override
    public List<Forecast> getAllForecastsFromProvider(Provider provider) {
        return forecastRepository.findByProvider(provider);
    }

    @Override
    public List<String> getListSeparatedIds(String date)
            throws ForecastNotFoundInDBException {
        //create text line of ids pairs "forecast,actual" ("1,2;3,4;...)
        List<String> listIds = new ArrayList<>();

        String exceptionMessage = "There are no %s from " + date + " in DB. " +
                "Please update database for this date before analysis.";

        List<Forecast> allDateForecasts = forecastRepository.findByDate(date);
        if (allDateForecasts == null || allDateForecasts.size() == 0)
            throw new ForecastNotFoundInDBException(String.format(exceptionMessage, "any forecasts or actuals"));

        Map<Provider, List<Forecast>> mapByProviders = allDateForecasts.stream()
                .collect(Collectors.groupingBy(Forecast::getProvider));

        for (List<Forecast> list : mapByProviders.values())
            listIds.add(createPairIds(exceptionMessage, list.stream()
                    .collect(Collectors.groupingBy(Forecast::forecastOrActual))));

        return listIds;
    }

    @Override
    public void saveNewDiff(Diff diff) {
        diffRepository.save(diff);
    }

    @Override
    public AverageDiff getAverageDiff(Provider provider) {
        return averageDiffRepository.findByProvider(provider);
    }

    @Override
    public List<AverageDiff> getAllAverageDiffs() {
        return averageDiffRepository.findAll();
    }

    @Override
    public List<AverageDiff> updateAverageDiffForAllDays() {
        List<AverageDiff> allAverageDiff = new ArrayList<>();

        setZeroForAllAverageDiff(); // ATTENTION !!!

        for (Provider provider : Provider.values()) {
            List<Diff> listDiffs = diffRepository.findByProvider(provider);

            if (listDiffs != null && listDiffs.size() > 0)
                for (Diff diff : listDiffs)
                    allAverageDiff.add(createAverageDiff(diff));
        }
        return allAverageDiff;
    }

    @Override
    public List<TesterAverage> createListAverageTesters(String date) {

        return Arrays.asList(Provider.values()).stream()
                .map(p -> createTesterAverage(date, p))
                .filter(tester -> tester != null)
                .sorted((t1, t2) -> (int) Double.parseDouble(t1.getValueDay()) - (int) Double.parseDouble(t2.getValueDay()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Provider, List<TesterItem>> createMapItemTesters(String ids) {
        Map<Provider, List<TesterItem>> mapItemTesters = new HashMap<>();

        for (String pair : ids.split(";")) {
            Long idForecast = Long.parseLong(pair.split(",")[0]);
            Long idActual = Long.parseLong(pair.split(",")[1]);
            Provider provider = forecastRepository.findById(idForecast).getProvider();

            mapItemTesters.put(provider, createDayListItemTestersByIds(idForecast, idActual));
        }
        return mapItemTesters;
    }

    //todo usable or delete
    /*@Override
    public List<Integer> createListOfAverageItems() {
        List<Diff> allDiffs = diffRepository.findAll();

        List<Integer> averageItems = new ArrayList<>();
        averageItems.add((int) allDiffs.stream().mapToDouble(Diff::getTempDiff).average().getAsDouble());
        averageItems.add((int) allDiffs.stream().mapToDouble(Diff::getPressureDiff).average().getAsDouble());
        averageItems.add((int) allDiffs.stream().mapToDouble(Diff::getCloudsDiff).average().getAsDouble());
        averageItems.add((int) allDiffs.stream().mapToDouble(Diff::getWindSpeedDiff).average().getAsDouble());
        averageItems.add((int) allDiffs.stream().mapToDouble(Diff::getDescriptionDiff).average().getAsDouble());

        return averageItems;
    }*/

    @Override
    public List<Diff> createListDiffsForPeriod(LocalDate from, LocalDate to) {

        return createListDatesOfPeriod(from, to).stream()
                .map(this::findBestDiffForDate)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Provider, List<Forecast>> createMapProviderForecastsForPeriod(LocalDate from, LocalDate to) {
        Map<Provider, List<Forecast>> mapForecasts = new HashMap<>();

        for (Provider provider : Provider.values())
            mapForecasts.put(provider, getForecastsByProviderForPeriod(provider, from, to));

        return mapForecasts;
    }

    @Override
    public List<String> createListStringDatesOfPeriod(LocalDate from, LocalDate to) {

        return createListDatesOfPeriod(from, to).stream()
                .map(DateTimeFormatter.ofPattern("yyyy/MM/dd")::format)
                .collect(Collectors.toList());
    }

    private String createPairIds(String exceptionMessage, Map<String, List<Forecast>> map)
            throws ForecastNotFoundInDBException {//list.size() = 1 in map always!

        List<Forecast> forecast = map.get("forecast");
        List<Forecast> actual = map.get("actual");

        if (forecast == null)
            throw new ForecastNotFoundInDBException(String.format(exceptionMessage, "forecast"));
        else if (actual == null)
            throw new ForecastNotFoundInDBException(String.format(exceptionMessage, "actual weather"));

        return forecast.get(0).getId().toString() + "," + actual.get(0).getId().toString();
    }

    private List<Forecast> getNewForecastsFromProvider(Provider provider)
            throws URISyntaxException, IOException, ParseException, NoDataFromProviderException {

        URI uri = new URI(provider.getLinkForecast());
        URL url = uri.toURL();

        String json;
        try {
            json = NetUtils.urlToString(url);
        } catch (Exception e) {
            throw new NoDataFromProviderException("Error getting data from provider " + provider.name()
                    + ". Try connect later");
        }

        //foreca has 1 json for all forecasts and actual (and has limit )
        if (provider == Provider.FORECA)
            return createListForecastsAndActualFromForeca(json);

        return forecastFactory.createListForecastModelsFromJson(provider, json);
    }

    private List<Forecast> createListForecastsAndActualFromForeca(String json)
            throws ParseException, NoDataFromProviderException {

        List<Forecast> list = forecastFactory.createListForecastModelsFromJson(Provider.FORECA, json);
        list = saveListNewForecasts(list);

        if (needUpdateActuals(Provider.FORECA)) {
            Forecast actual = actualFactory.createActualModelFromJson(Provider.FORECA, json);;
            saveNewForecast(actual);
            createAndSaveNewDiff(actual);
        }
        return list;
    }

    private List<Forecast> createListForecastsFromProvider(Provider provider)
            throws NoDataFromProviderException {
        List<Forecast> list;

        try {
            list = getNewForecastsFromProvider(provider);

            if (provider != Provider.FORECA)
                list = saveListNewForecasts(list);

        } catch (Exception e) {
            throw new NoDataFromProviderException("There is no data from provider " + provider.name());
        }
        return list;
    }

    private List<Forecast> saveListNewForecasts(List<Forecast> list)
            throws NoDataFromProviderException {

        list = validateNewForecasts(list);
        if (list.size() > 0)
            for (Forecast forecast : list)
                saveNewForecast(forecast);

        return list;
    }


    private TesterAverage createTesterAverage(String date, Provider provider) {
        TesterAverage testerAverage = null;

        Diff diff = diffRepository.findByDateAndProvider(date, provider);

        if (diff != null) {
            AverageDiff averageDiff = averageDiffRepository.findByProvider(provider);

            testerAverage = new TesterAverage(
                    provider,
                    date,
                    String.valueOf(diff.getAverageDayDiff()),
                    String.valueOf(averageDiff.getValue()),
                    String.valueOf(averageDiff.getDays())
            );
        }

        return testerAverage;
    }

    private List<Forecast> getForecastsByProviderForPeriod(Provider provider, LocalDate from, LocalDate to) {

        return createListStringDatesOfPeriod(from, to).stream()
                .map(date -> forecastRepository.findByDateAndProviderAndActual(date, provider, false))
                .filter(forecast -> forecast != null)
                .sorted((forecast1, forecast2) -> forecast1.getId().intValue() - forecast2.getId().intValue())
                .collect(Collectors.toList());
    }

    private Diff findBestDiffForDate(LocalDate localDate) {
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        List<Diff> currentDateDiffs = diffRepository.findByDate(date);
        currentDateDiffs.sort((diff1, diff2) -> (int) (diff1.getAverageDayDiff() - diff2.getAverageDayDiff()));

        return currentDateDiffs.size() > 0 ? currentDateDiffs.get(0) : null;
    }

    private List<LocalDate> createListDatesOfPeriod(LocalDate from, LocalDate to) {

        return Stream.iterate(from.plusDays(1), day -> day.plusDays(1))
                .limit(Period.between(from, to).getDays())
                .collect(Collectors.toList());
    }

    private List<TesterItem> createDayListItemTestersByIds(Long idForecast, Long idActual) {

        Forecast forecast = forecastRepository.findById(idForecast);
        Forecast actual = forecastRepository.findById(idActual);
        Diff diff = diffRepository.findByDateAndProvider(actual.getDate(), actual.getProvider());

        return Constants.FIELDS_AND_RANGES_MAP.keySet().stream()
                .map((name) -> new TesterItem(
                        name,
                        actual.getProvider(),
                        actual.getDate(),
                        forecast.determineFieldByString(name),
                        actual.determineFieldByString(name),
                        diff.determineFieldByString(name)
                )).collect(Collectors.toList());
    }

    private Diff createAndSaveNewDiff(Forecast actual) {
        Diff diff = checkForecastInDB(actual) ?
                calculateDiff(actual) :
                null ;

        if (diff != null && !checkDiffInDB(diff)) {
            diff.setInclInAverageDiff(true);
            saveNewDiff(diff);
            createAverageDiff(diff);
        }
        return diff;
    }

    private Diff calculateDiff(Forecast actual) {
        Forecast forecast = forecastRepository.findByDateAndProviderAndActual(
                actual.getDate(), actual.getProvider(), false);
        if (forecast == null)
                return null;

        return new Diff(forecast, actual);
    }

    private AverageDiff createAverageDiff(Diff diff) {
        AverageDiff averageDiff = averageDiffRepository.findByProvider(diff.getProvider());

        if (averageDiff == null)
            averageDiff = new AverageDiff(diff);
        else
            averageDiff = averageDiff.addDiff(diff);

        return averageDiffRepository.save(averageDiff);
    }

    private void setZeroForAllAverageDiff() {
        List<AverageDiff> listAll = averageDiffRepository.findAll();

        if (listAll != null && listAll.size() > 0) {
            for (AverageDiff diff : listAll) {
                diff.setValue(0.0);
                diff.setDays(0);

                averageDiffRepository.save(diff);
            }
        }
    }

    //clear list forecast for better save
    private List<Forecast> validateNewForecasts(List<Forecast> list) {
        List<Forecast> listNewForecasts = new ArrayList<>();

        for (Forecast forecast : list)
            if (!forecast.isActual() && !checkForecastInDB(forecast))
                listNewForecasts.add(forecast);
            else if (forecast.isActual() && !checkActualInDB(forecast))
                listNewForecasts.add(forecast);

        return listNewForecasts;
    }

    private boolean checkForecastInDB(Forecast forecast) {
        return forecastRepository.findByDateAndProviderAndActual(
                forecast.getDate(), forecast.getProvider(), false) != null;
    }

    private boolean checkActualInDB(Forecast actual) {
        return forecastRepository.findByDateAndProviderAndActual(
                actual.getDate(), actual.getProvider(), true) != null;
    }

    private boolean checkDiffInDB(Diff diff) {
        return diffRepository.findByDateAndProvider(diff.getDate(), diff.getProvider()) != null;
    }

    private boolean needUpdateForecasts(Provider provider) {
        int maxDays = provider.getMaxDaysForecast();

        for (int i = 1; i < maxDays; i++) {
            long addingDays = i * 86400000; //86400000 is 24 hours
            String testDate = new java.text.SimpleDateFormat("yyyy/MM/dd")
                    .format(System.currentTimeMillis() + addingDays);

            if (forecastRepository.findByDateAndProviderAndActual(testDate, provider, false) == null)
                return true;
        }
        return false;
    }

    private boolean needUpdateActuals(Provider provider) {
        String today = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(System.currentTimeMillis());

        return forecastRepository.findByDateAndProviderAndActual(today, provider, true) == null;
    }

    /*//test
    public static void main(String[] args) {

        ParseUtils parseUtils = new ParseUtils();
        Provider provider = Provider.FORECA;

        try {
            URI uri = new URI(provider.getLinkForecast());
            URL url = uri.toURL();
            String json = NetUtils.urlToString(url);
            System.out.println("FORECAST: " + json);

            *//*List<Forecast> forecasts = parseUtils.createListForecastModelsFromJson(provider, json);
            System.out.println("FORECASTS:");
            forecasts.stream().forEach(System.out::println);*//*

            uri = new URI(provider.getLinkActual());
            url = uri.toURL();
            json = NetUtils.urlToString(url);
            System.out.println("CURRENT: " + json);

            *//*Forecast forecast = parseUtils.createActualModelFromJson(provider, json);
            System.out.println("DATE " + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + " ACTUAL:");
            System.out.println(forecast);*//*

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
}
