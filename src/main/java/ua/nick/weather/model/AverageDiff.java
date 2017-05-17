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

    public AverageDiff(Provider provider, int days,
                       double tempTotalDiff, double pressureTotalDiff, double cloudsTotalDiff,
                       double windSpeedTotalDiff, double descrTotalDiff, double value) {
        this.provider = provider;
        this.days = days;
        this.tempTotalDiff = tempTotalDiff;
        this.pressureTotalDiff = pressureTotalDiff;
        this.cloudsTotalDiff = cloudsTotalDiff;
        this.windSpeedTotalDiff = windSpeedTotalDiff;
        this.descrTotalDiff = descrTotalDiff;
        this.value = value;
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
}
