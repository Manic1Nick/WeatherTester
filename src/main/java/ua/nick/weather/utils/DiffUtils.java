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

        return Math.round(tempDiff * 100 / mapDiffs.get("Temp"));
    }

    public double calculatePressureDiff(int pressureForecast, int pressureActual) {

        double pressureDiff = pressureForecast != 0 ?
               pressureActual / pressureForecast : 1000;

        return Math.round(pressureDiff);
    }

    public double calculateCloudsDiff(int cloudsForecast, int cloudsActual) {

        double cloudsDiff = cloudsActual - cloudsForecast;

        return Math.round(cloudsDiff);
    }

    public double calculateWindSpeedDiff(int windSpeedForecast, int windSpeedActual) {

        double windSpeedDiff = windSpeedActual - windSpeedForecast;

        return Math.round(windSpeedDiff * 100 / mapDiffs.get("WindSpeed"));
    }

    public double calculateDescriptionDiff(String descriptionForecast, String descriptionActual) {

        double descriptionDiff = determineDescriptionDiff(descriptionForecast, descriptionActual);

        return (double) (Math.round(descriptionDiff * 10)) / 10;
    }

    public double calculateAverageDayDiff(double tempDiff, double pressureDiff,
                double cloudsDiff, double windSpeedDiff, double descriptionDiff) {

        double averageDayDiff = Math.abs(tempDiff) * mapShares.get("Temp") +
                Math.abs(pressureDiff) * mapShares.get("Pressure") +
                Math.abs(cloudsDiff) * mapShares.get("Clouds") +
                Math.abs(windSpeedDiff) * mapShares.get("WindSpeed") +
                Math.abs(descriptionDiff) * mapShares.get("Description");

        return Math.round(averageDayDiff);
    }

    public double calculateAverageValue(double value, double addingValue, int days) {

        double result = (days * value + Math.abs(addingValue)) / (days + 1);

        return (double) (Math.round(result * 10)) / 10;
    }

    private Double determineDescriptionDiff(String forecastDescription, String actualDescription) {

        List<String> keyWords = Arrays.asList(actualDescription.toLowerCase().split(" ")).stream()
                .filter(word -> word.length() > 3).collect(Collectors.toList());

        return (double) keyWords.stream()
                .filter(word -> !forecastDescription.toLowerCase().contains(word))
                .count()
                / keyWords.size() * 100;
    }
}
