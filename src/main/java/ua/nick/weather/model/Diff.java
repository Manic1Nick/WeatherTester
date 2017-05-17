package ua.nick.weather.model;

import javax.persistence.*;

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

    public Diff(String date, Provider provider,
                double tempDiff, double pressureDiff, double cloudsDiff,
                double windSpeedDiff, double descriptionDiff, double averageDayDiff) {
        this.date = date;
        this.provider = provider;
        this.tempDiff = tempDiff;
        this.pressureDiff = pressureDiff;
        this.cloudsDiff = cloudsDiff;
        this.windSpeedDiff = windSpeedDiff;
        this.descriptionDiff = descriptionDiff;
        this.averageDayDiff = averageDayDiff;
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
}
