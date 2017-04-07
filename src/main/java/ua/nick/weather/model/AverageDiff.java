package ua.nick.weather.model;

import javax.persistence.*;

@Entity
@Table(name = "averages")
public class AverageDiff {

    private Long id;
    private Provider provider;
    private int days;

    private double tempTotalDiff;
    private double pressureTotalDiff;
    private double cloudsTotalDiff;
    private double windSpeedTotalDiff;
    private double descrTotalDiff;

    private double value;

    public AverageDiff() {
    }

    public AverageDiff(Provider provider) {
        this.provider = provider;
    }

    public AverageDiff(Diff diff) {
        this.provider = diff.getProvider();
        this.days = 1;
        this.tempTotalDiff = diff.getTempDiff();
        this.pressureTotalDiff = diff.getPressureDiff();
        this.cloudsTotalDiff = diff.getCloudsDiff();
        this.windSpeedTotalDiff = diff.getWindSpeedDiff();
        this.descrTotalDiff = diff.getDescriptionDiff();
        this.value = diff.getAverageDayDiff();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public double getTempTotalDiff() {
        return tempTotalDiff;
    }

    public void setTempTotalDiff(double tempTotalDiff) {
        this.tempTotalDiff = tempTotalDiff;
    }

    public double getPressureTotalDiff() {
        return pressureTotalDiff;
    }

    public void setPressureTotalDiff(double pressureTotalDiff) {
        this.pressureTotalDiff = pressureTotalDiff;
    }

    public double getCloudsTotalDiff() {
        return cloudsTotalDiff;
    }

    public void setCloudsTotalDiff(double cloudsTotalDiff) {
        this.cloudsTotalDiff = cloudsTotalDiff;
    }

    public double getWindSpeedTotalDiff() {
        return windSpeedTotalDiff;
    }

    public void setWindSpeedTotalDiff(double windSpeedTotalDiff) {
        this.windSpeedTotalDiff = windSpeedTotalDiff;
    }

    public double getDescrTotalDiff() {
        return descrTotalDiff;
    }

    public void setDescrTotalDiff(double descrTotalDiff) {
        this.descrTotalDiff = descrTotalDiff;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Transient
    public String getDetails() {
        return "Temperature: " + tempTotalDiff +
                "%,<br /> Pressure: " + pressureTotalDiff +
                "%,<br /> Clouds: " + cloudsTotalDiff +
                "%,<br /> Wind speed: " + windSpeedTotalDiff +
                "%,<br /> Description: " + descrTotalDiff +
                "%<br /> ";
    }

    public AverageDiff addDiff(Diff diff) {

        double temp = (days * this.tempTotalDiff + Math.abs(diff.getTempDiff())) / (days + 1);
        double pressure = (days * this.pressureTotalDiff + Math.abs(diff.getPressureDiff())) / (days + 1);
        double clouds = (days * this.cloudsTotalDiff + Math.abs(diff.getCloudsDiff())) / (days + 1);
        double windSpeed = (days * this.windSpeedTotalDiff + Math.abs(diff.getWindSpeedDiff())) / (days + 1);
        double description = (days * this.descrTotalDiff + Math.abs(diff.getDescriptionDiff())) / (days + 1);
        double value = (days * this.value + Math.abs(diff.getAverageDayDiff())) / (days + 1);

        setTempTotalDiff((double) (Math.round(temp * 10)) / 10);
        setCloudsTotalDiff((double) (Math.round(clouds * 10)) / 10);
        setPressureTotalDiff((double) (Math.round(pressure * 10)) / 10);
        setWindSpeedTotalDiff((double) (Math.round(windSpeed * 10)) / 10);
        setDescrTotalDiff((double) (Math.round(description * 10)) / 10);
        setValue((double) (Math.round(value * 10)) / 10);

        setDays(days + 1);

        return this;
    }

}
