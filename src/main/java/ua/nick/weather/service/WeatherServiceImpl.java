package ua.nick.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import ua.nick.weather.utils.ParseUtils;
import ua.nick.weather.weatherFactory.ActualWeatherFactory;
import ua.nick.weather.weatherFactory.ForecastFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        for (Provider provider : Provider.values()) {

            if (needUpdateForecasts(provider)) {
                try {
                    List<Forecast> list = getNewForecastsFromProvider(provider);

                    if (provider != Provider.FORECA)
                        list = saveListNewForecasts(list);

                    allForecasts.add(list);

                } catch (Exception e) {
                    throw new NoDataFromProviderException("There is no data from provider " + provider.name());
                }
            }
        }
        return allForecasts;
    }

    private List<Forecast> saveListNewForecasts(List<Forecast> list) throws NoDataFromProviderException {

        list = validateNewForecasts(list);
        if (list.size() > 0)
            for (Forecast forecast : list)
                saveNewForecast(forecast);

        return list;
    }

    @Override
    public List<Forecast> getNewForecastsFromProvider(Provider provider)
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
        //actuals = addOtherTodayActualsToList(actuals);

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
    public List<List<Long>> getListForecastsAndActualsIds(String date)
            throws ForecastNotFoundInDBException {
        List<List<Long>> list = new ArrayList<>();

        String exceptionMessage = "There are no %s from " + date + " in DB. " +
                "Please update database for this date before analysis.";

        List<Forecast> allForecastsByDay = forecastRepository.findByDate(date);
        if (allForecastsByDay == null || allForecastsByDay.size() == 0)
            throw new ForecastNotFoundInDBException(String.format(exceptionMessage, "any forecasts or actuals"));

        Map<Provider, Map<String, Forecast>> mapByProviders = distributeForecatsByProviders(allForecastsByDay);
        for (Map<String, Forecast> map : mapByProviders.values()) {
            Forecast forecast = map.get("forecast");
            Forecast actual = map.get("actual");
            if (forecast == null)
                throw new ForecastNotFoundInDBException(String.format(exceptionMessage, "forecast"));
            else if (actual == null)
                throw new ForecastNotFoundInDBException(String.format(exceptionMessage, "actual weather"));
            else
                list.add(Arrays.asList(forecast.getId(), actual.getId()));
        }
        return list;
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

        for (Provider provider : Arrays.asList(Provider.values())) {
            List<Diff> listDiffs = diffRepository.findByProvider(provider);

            if (listDiffs != null && listDiffs.size() > 0)
                for (Diff diff : listDiffs)
                    allAverageDiff.add(createAverageDiff(diff));
        }
        return allAverageDiff;
    }

    @Override
    public List<TesterAverage> createListAverageTesters(String date) {
        List<TesterAverage> averageTesters = new ArrayList<>();

        for (Provider provider : Arrays.asList(Provider.values())) {
            TesterAverage testerAverage = createTesterAverage(date, provider);

            if (testerAverage != null)
                averageTesters.add(testerAverage);
        }
        averageTesters.sort((avTester1, avTester2) ->
                (int) (Math.round(Double.parseDouble(avTester1.getValueDay())) -
                        Math.round(Double.parseDouble(avTester2.getValueDay()))));

        return averageTesters;
    }

    @Override
    public Map<Provider, List<TesterItem>> createMapItemTesters(String ids) {
        Map<Provider, List<TesterItem>> mapItemTesters = new HashMap<>();

        String[] pairs = ids.split(";");
        for (String pair : pairs) {
            Long idForecast = Long.parseLong(pair.split(",")[0]);
            Long idActual = Long.parseLong(pair.split(",")[1]);
            Provider provider = forecastRepository.findById(idForecast).getProvider();
            mapItemTesters.put(provider, createDayListItemTestersByIds(idForecast, idActual));
        }

        return mapItemTesters;
    }

    //todo usable or delete
    @Override
    public List<Integer> createListOfAverageItems() {
        List<Integer> averageItems = new ArrayList<>();

        int temp = 0;
        int pressure = 0;
        int clouds = 0;
        int windSpeed = 0;
        int description = 0;

        List<Diff> allDiffs = diffRepository.findAll();
        for (Diff diff : allDiffs) {
            temp += Math.abs(diff.getTempDiff());
            pressure += Math.abs(diff.getPressureDiff());
            clouds += Math.abs(diff.getCloudsDiff());
            windSpeed += Math.abs(diff.getWindSpeedDiff());
            description += Math.abs(diff.getDescriptionDiff());
        }

        int size = allDiffs.size();
        averageItems.add(temp / size);
        averageItems.add(pressure / size);
        averageItems.add(clouds / size);
        averageItems.add(windSpeed / size);
        averageItems.add(description / size);

        return averageItems;
    }

    @Override
    public List<Diff> createListDiffsForPeriod(LocalDate from, LocalDate to) {
        List<Diff> diffs = new ArrayList<>();

        List<LocalDate> datesOfPeriod = createListDatesOfPeriod(from, to);

        for (LocalDate localDate : datesOfPeriod)
            diffs.add(findBestDiffForDate(localDate));

        return diffs;
    }

    @Override
    public Map<Provider, List<Forecast>> createMapProviderForecastsForPeriod(LocalDate from, LocalDate to) {
        Map<Provider, List<Forecast>> mapForecasts =new HashMap<>();

        for (Provider provider : Provider.values())
            mapForecasts.put(provider, getForecastsByProviderForPeriod(provider, from, to));

        return mapForecasts;
    }

    @Override
    public List<String> createListStringDatesOfPeriod(LocalDate from, LocalDate to) {
        List<String> dates = new ArrayList<>();

        int end = to.getDayOfYear() - from.getDayOfYear() + 1;
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        for (int i = 0; i < end; i++)
            dates.add(from.plusDays(i).format(yyyyMMdd));

        return dates;
    }


    private TesterAverage createTesterAverage(String date, Provider provider) {
        TesterAverage testerAverage = null;

        Diff diff = diffRepository.findByDateAndProvider(date, provider);

        if (diff != null) {
            AverageDiff averageDiff = averageDiffRepository.findByProvider(provider);

            testerAverage = new TesterAverage(
                    provider, date, String.valueOf(diff.getAverageDayDiff()),
                    String.valueOf(averageDiff.getValue()), String.valueOf(averageDiff.getDays()));
        }

        return testerAverage;
    }

    private List<Forecast> addOtherTodayActualsToList(List<Forecast> actuals) {

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        List<Forecast> allActuals = forecastRepository.findByDateAndActual(today, true);
        for (Forecast forecast : allActuals)
            if (!actuals.contains(forecast))
                actuals.add(forecast);

        return actuals;
    }

    private List<Forecast> getForecastsByProviderForPeriod(Provider provider, LocalDate from, LocalDate to) {
        List<Forecast> forecasts = new ArrayList<>();

        List<String> datesOfPeriod = createListStringDatesOfPeriod(from, to);

        for (String date : datesOfPeriod) {
            Forecast current = forecastRepository.findByDateAndProviderAndActual(date, provider, false);
            if (current != null)
                forecasts.add(current);
        }
        forecasts.sort((forecast1, forecast2) -> forecast1.getId().intValue() - forecast2.getId().intValue());

        return forecasts;
    }

    private Diff findBestDiffForDate(LocalDate localDate) {
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        List<Diff> currentDateDiffs = diffRepository.findByDate(date);
        currentDateDiffs.sort((diff1, diff2) -> (int) (diff1.getAverageDayDiff() - diff2.getAverageDayDiff()));

        return currentDateDiffs.size() > 0 ? currentDateDiffs.get(0) : null;
    }

    private List<LocalDate> createListDatesOfPeriod(LocalDate from, LocalDate to) {
        List<LocalDate> localDates = new ArrayList<>();

        int end = to.getDayOfYear() - from.getDayOfYear() + 1;

        for (int i = 1; i < end; i++)
            localDates.add(from.plusDays(i));

        localDates.stream().sorted();

        return localDates;
    }

    private List<LocalDate> createListDatesOfCurrentWeek() {
        List<LocalDate> localDates = new ArrayList<>();

        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();

        for (int i = dayOfWeek-1; i >= 0; i--)
            localDates.add(today.minusDays(i));

        for (int i = dayOfWeek; i < 7; i++)
            localDates.add(today.plusDays(i));

        localDates.stream().sorted();

        return localDates;
    }

    private List<TesterItem> createDayListItemTestersByIds(Long idForecast, Long idActual) {
        List<TesterItem> itemTesters = new ArrayList<>(); //size = fields value in forecast

        Forecast forecast = forecastRepository.findById(idForecast);
        Forecast actual = forecastRepository.findById(idActual);
        Diff diff = diffRepository.findByDateAndProvider(actual.getDate(), actual.getProvider());

        for (String name : Constants.FIELDS_AND_RANGES_MAP.keySet())
            itemTesters.add(
                    new TesterItem(
                            name,
                            actual.getProvider(),
                            actual.getDate(),
                            forecast.determineFieldByString(name),
                            actual.determineFieldByString(name),
                            diff.determineFieldByString(name)
                    )
            );

        return itemTesters;
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

        for (AverageDiff diff : listAll) {
            diff.setValue(0.0);
            diff.setDays(0);

            averageDiffRepository.save(diff);
        }
    }

    private Map<Provider, Map<String, Forecast>> distributeForecatsByProviders(List<Forecast> list) {
        Map<Provider, Map<String, Forecast>> mapByProviders = new HashMap<>();
        Map<String, Forecast> map;

        for (Forecast forecast : list) {
            map = mapByProviders.get(forecast.getProvider());
            
            if (map == null) {
                map = new HashMap<>();
                mapByProviders.put(forecast.getProvider(), map);
            }
            
            if (forecast.isActual() && map.get("actual") == null)
                map.put("actual", forecast);
            else if (!forecast.isActual() && map.get("forecast") == null)
                map.put("forecast", forecast);
        }
        return mapByProviders;
    }

    private Forecast getForecastByDateProviderActual(String date, Provider provider)
            throws ForecastNotFoundInDBException {

        Forecast forecast = forecastRepository.findByDateAndProviderAndActual(date, provider, false);

        if (forecast == null)
            throw new ForecastNotFoundInDBException("There is no forecast with date " + date + " in DB. " +
                    "Please update forecast for this date before analysis.");

        return forecast;
    }

    private Forecast getActualByDateProviderActual(String date, Provider provider)
            throws ForecastNotFoundInDBException {
        Forecast forecast;

        List<Forecast> actuals = forecastRepository.findByDateAndProviderAndActual(
                date, provider, true, new PageRequest(0, 1));

        if (actuals == null || actuals.size() == 0)
            throw new ForecastNotFoundInDBException("There is no actual weather with date " + date + " in DB. " +
                            "Please update actual weather before analysis.");
        else
            forecast = actuals.get(0);

        return forecast;
    }


    //clear list forecast for better save
    private List<Forecast> validateNewForecasts(List<Forecast> list) {
        List<Forecast> listNewForcasts = new ArrayList<>();

        for (Forecast forecast : list)
            if (!forecast.isActual() && !checkForecastInDB(forecast))
                listNewForcasts.add(forecast);
            else if (forecast.isActual() && !checkActualInDB(forecast))
                listNewForcasts.add(forecast);

        return listNewForcasts;
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

    /*@Override
    public Map<Provider, List<Forecast>> createMapForecastsByProviders(boolean actual) {
        Map<Provider, List<Forecast>> map = new HashMap<>();

        for (Provider provider : Arrays.asList(Provider.values())) {
            List<Forecast> list = getAllForecastsFromProviderAndActual(provider, actual);
            if (list != null) {
                list.sort((o1, o2) -> Long.compare(o1.getTimeUnix(), o2.getTimeUnix()));
                map.put(provider, list);
            }
        }
        return map;
    }

    @Override
    public Map<Provider, Map<String, Map<Tester, String>>> createDayMapForecastsByProviders(
            String separatedByCommaIds) {

        Map<Provider, Map<String, Map<Tester, String>>> mapProviders = new HashMap<>();
        //provider -> {item -> {tester -> value}}

        String[] pairs = separatedByCommaIds.split(";");
        for (String pair : pairs) {
            Long id = Long.parseLong(pair.split(",")[0]);
            Provider provider = forecastRepository.findById(id).getProvider();
            mapProviders.put(provider, createDayMapForecastsByIds(pair));
        }
        return mapProviders;
    }

    @Override
    public Diff getDiffByDateAndProvider(String date, Provider provider) {
        return diffRepository.findByDateAndProvider(date, provider);
    }

    @Override
    public void saveNewAverageDiff(AverageDiff diff) {
        averageDiffRepository.save(diff);
    }

    @Override
    public List<Forecast> getAllForecastsFromProviderAndActual(Provider provider, boolean actual) {
        return forecastRepository.findByProviderAndActual(provider, actual);
    }

    @Override
    public Map<String, Map<Tester, String>> createDayMapForecastsByIds(String separatedByCommaIds) {
        Map<String, Map<Tester, String>> mapItems = new HashMap<>(); //item -> {tester -> value}

        String[] ids = separatedByCommaIds.split(",");

        Forecast forecast = forecastRepository.findById(Long.parseLong(ids[0]));
        Forecast actual = forecastRepository.findById(Long.parseLong(ids[1]));

        Diff diff = diffRepository.findByDateAndProvider(forecast.getDate(), forecast.getProvider());
        if (diff == null)
            diff = createAndSaveNewDiff(forecast, actual);

        for (String item : Constants.FIELDS_AND_RANGES_MAP.keySet()) {
            SortedMap<Tester, String> mapForecasts = new TreeMap<>();
            item = item.toLowerCase();

            mapForecasts.put(Tester.FORECAST, forecast.determineFieldByString(item));
            mapForecasts.put(Tester.ACTUAL, actual.determineFieldByString(item));
            mapForecasts.put(Tester.DIFFERENT, diff.determineFieldByString(item));

            mapItems.put(item, mapForecasts);
        }
        return mapItems;
    }

    @Override
    public Diff createAndSaveNewDiff(Forecast forecast, Forecast actual) {
        Diff diff = new Diff(forecast, actual);
        saveNewDiff(diff);

        return diff;
    }*/

    //test
    public static void main(String[] args) {

        ParseUtils parseUtils = new ParseUtils();
        Provider provider = Provider.FORECA;

        try {
            URI uri = new URI(provider.getLinkForecast());
            URL url = uri.toURL();
            String json = NetUtils.urlToString(url);
            System.out.println("FORECAST: " + json);

            /*List<Forecast> forecasts = parseUtils.createListForecastModelsFromJson(provider, json);
            System.out.println("FORECASTS:");
            forecasts.stream().forEach(System.out::println);*/

            uri = new URI(provider.getLinkActual());
            url = uri.toURL();
            json = NetUtils.urlToString(url);
            System.out.println("CURRENT: " + json);

            /*Forecast forecast = parseUtils.createActualModelFromJson(provider, json);
            System.out.println("DATE " + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + " ACTUAL:");
            System.out.println(forecast);*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
