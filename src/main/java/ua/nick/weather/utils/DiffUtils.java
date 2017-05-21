package ua.nick.weather.utils;

import ua.nick.weather.model.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiffUtils {

    private Map<String, Integer> mapDiffs;
    private Map<String, Double> mapShares;

    public DiffUtils() {
        mapDiffs = Constants.FIELDS_AND_RANGES_MAP;
        mapShares = Constants.FIELDS_AND_SHARES_MAP;
    }

    public double calculateTempDiff(int tempMinForecast, int tempMaxForecast, int tempMinActual, int tempMaxActual) {

        double tempDiff = ((tempMinForecast + tempMaxForecast) -
                (tempMinActual + tempMaxActual)) / 2;

        return roundDouble(tempDiff * 100 / mapDiffs.get("Temp"));
    }

    public double calculatePressureDiff(int pressureForecast, int pressureActual) {

        double pressureDiff = pressureForecast != 0 ?
                pressureActual - pressureForecast
                : pressureActual - 1000;

        return roundDouble(pressureDiff * 100 / mapDiffs.get("Pressure"));
    }

    public double calculateCloudsDiff(int cloudsForecast, int cloudsActual) {

        double cloudsDiff = cloudsActual - cloudsForecast;

        return roundDouble(cloudsDiff);
    }

    public double calculateWindSpeedDiff(int windSpeedForecast, int windSpeedActual) {

        double windSpeedDiff = windSpeedActual - windSpeedForecast;

        return roundDouble(windSpeedDiff * 100 / mapDiffs.get("WindSpeed"));
    }

    public double calculateDescriptionDiff(String descriptionForecast, String descriptionActual) {

        double descriptionDiff = determineDescriptionDiff(descriptionForecast, descriptionActual);

        return roundDouble(descriptionDiff);
    }

    public double calculateAverageDayDiff(double tempDiff, double pressureDiff,
                double cloudsDiff, double windSpeedDiff, double descriptionDiff) {

        double averageDayDiff = Math.abs(tempDiff) * mapShares.get("Temp") +
                Math.abs(pressureDiff) * mapShares.get("Pressure") +
                Math.abs(cloudsDiff) * mapShares.get("Clouds") +
                Math.abs(windSpeedDiff) * mapShares.get("WindSpeed") +
                Math.abs(descriptionDiff) * mapShares.get("Description");

        return roundDouble(averageDayDiff);
    }

    public double calculateAverageValue(double value, double addingValue, int days) {

        double result = (days * value + Math.abs(addingValue)) / (days + 1);

        return roundDouble(result);
    }
    private Double determineDescriptionDiff(String forecastDescription, String actualDescription) {

        List<String> forecastWords = createListForecastWordsFromString(forecastDescription);
        List<String> actualWords = createListForecastWordsFromString(actualDescription);

        boolean forecastBiggerThanActual = forecastWords.size() > actualWords.size();
        List<String> baseWords = forecastBiggerThanActual ? forecastWords : actualWords ;
        List<String> keyWords = !forecastBiggerThanActual ? forecastWords : actualWords ;

        int nonrepeatableWords = (int) keyWords.stream().filter(word -> !baseWords.contains(word)).count();

        return (double) nonrepeatableWords / baseWords.size() * 100;
    }

    private double roundDouble(double n) {
        return (double) (Math.round(n * 10)) / 10;
    }

    private List<String> createListForecastWordsFromString(String forecast) {
        return Arrays.asList(forecast.toLowerCase().split(" ")).stream()
                .filter(word -> word.length() > 3).collect(Collectors.toList());
    }
}
