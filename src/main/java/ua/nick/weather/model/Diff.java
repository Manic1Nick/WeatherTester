package ua.nick.weather.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "diffs")
public class Diff {

    private Long id;
    private String date;
    private Provider provider;
    private double tempDiff;
    private double pressureDiff;
    private double cloudsDiff;
    private double windSpeedDiff;
    private double descriptionDiff;

    private double averageDayDiff;
    private boolean inclInAverageDiff;

    private Map<String, Integer> mapDiffs = Constants.FIELDS_AND_RANGES_MAP;
    private Map<String, Double> mapShares = Constants.FIELDS_AND_SHARES_MAP;

    public Diff() {
    }

    public Diff(String date) {
        this.date = date;
    }

    public Diff(Provider provider) {
        this.provider = provider;
    }

    public Diff(String date, Provider provider) {
        this.date = date;
        this.provider = provider;
    }

    public Diff(Forecast forecast, Forecast actual) {
        this.date = forecast.getDate();
        this.provider = forecast.getProvider();

        double diff = ((actual.getTempMin() + actual.getTempMax()) -
                (forecast.getTempMin() + forecast.getTempMax())) / 2;
        this.tempDiff = Math.round(diff * 100 / mapDiffs.get("Temp"));

        diff = forecast.getPressure() != 0 ?
                actual.getPressure() / forecast.getPressure() : 1000;
        this.pressureDiff = Math.round(diff);

        diff = actual.getClouds() - forecast.getClouds();
        this.cloudsDiff = Math.round(diff);

        diff = actual.getWindSpeed() - forecast.getWindSpeed();
        this.windSpeedDiff = Math.round(diff * 100 / mapDiffs.get("WindSpeed"));

        this.descriptionDiff = (double) (Math.round(
                determineDescriptionDiff(forecast.getDescription(), actual.getDescription()) * 10)) / 10;

        diff = Math.abs(tempDiff) * mapShares.get("Temp") +
                Math.abs(pressureDiff) * mapShares.get("Pressure") +
                Math.abs(cloudsDiff) * mapShares.get("Clouds") +
                Math.abs(windSpeedDiff) * mapShares.get("WindSpeed") +
                Math.abs(descriptionDiff) * mapShares.get("Description");
        this.averageDayDiff = Math.round(diff);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public double getTempDiff() {
        return tempDiff;
    }

    public void setTempDiff(double tempMinDiff) {
        this.tempDiff = tempMinDiff;
    }

    public double getPressureDiff() {
        return pressureDiff;
    }

    public void setPressureDiff(double pressureDiff) {
        this.pressureDiff = pressureDiff;
    }

    public double getCloudsDiff() {
        return cloudsDiff;
    }

    public void setCloudsDiff(double cloudsDiff) {
        this.cloudsDiff = cloudsDiff;
    }

    public double getWindSpeedDiff() {
        return windSpeedDiff;
    }

    public void setWindSpeedDiff(double windSpeedDiff) {
        this.windSpeedDiff = windSpeedDiff;
    }

    public double getDescriptionDiff() {
        return descriptionDiff;
    }

    public void setDescriptionDiff(double descriptionDiff) {
        this.descriptionDiff = descriptionDiff;
    }

    public double getAverageDayDiff() {
        return averageDayDiff;
    }

    public void setAverageDayDiff(double averageDayDiff) {
        this.averageDayDiff = averageDayDiff;
    }

    public boolean isInclInAverageDiff() {
        return inclInAverageDiff;
    }

    public void setInclInAverageDiff(boolean inclInAverageDiff) {
        this.inclInAverageDiff = inclInAverageDiff;
    }

    public String determineFieldByString(String item) {
        String value = "";
        item = item.toLowerCase();

        if (item.equals("date"))
            value = date;
        else if (item.equals("temp"))
            value = String.valueOf(tempDiff) + " %";
        else if (item.equals("pressure"))
            value = String.valueOf(pressureDiff) + " %";
        else if (item.equals("clouds"))
            value = String.valueOf(cloudsDiff) + " %";
        else if (item.equals("windspeed"))
            value = String.valueOf(windSpeedDiff) + " %";
        else if (item.equals("description"))
            value = String.valueOf(descriptionDiff) + " %";
        else if (item.equals("average"))
            value = String.valueOf(averageDayDiff) + " %";

        return value;
    }

    private static Double determineDescriptionDiff(String forecastDescription, String actualDescription) {

        String splittedString;
        String baseString;

        if (forecastDescription.split(" ").length > actualDescription.split(" ").length ) {
            splittedString = forecastDescription;
            baseString = actualDescription;
        } else {
            splittedString = actualDescription;
            baseString = forecastDescription;
        }

        List<String> keyWords = Arrays.asList(splittedString.toLowerCase().split(" ")).stream()
                .filter(word -> word.length() > 3).collect(Collectors.toList());

        return (double) keyWords.stream()
                .filter(word -> !baseString.toLowerCase().contains(word))
                .count()
                / keyWords.size() * 100;
    }
}
