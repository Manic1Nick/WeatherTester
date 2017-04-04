package ua.nick.weather.model;

import javax.persistence.*;

@Entity
@Table(name = "forecasts")
public class Forecast {

    private Long id;
    private long timeUnix;
    private String date;
    private int tempMin;
    private int tempMax;
    private int pressure;
    private int clouds;
    private int windSpeed;
    private String description;
    private Provider provider;
    private int daysBeforeActual;
    private boolean actual; //false for forecast, true for fact weather

    public Forecast() {
    }

    public Forecast(Provider provider) {
        this.provider = provider;
    }

    public Forecast(Provider provider, boolean actual) {
        this.provider = provider;
        this.actual = actual;
    }

    public Forecast(long timeUnix, String date, int tempMin, int tempMax, int pressure, int clouds, int windSpeed, String description, Provider provider, int daysBeforeActual, boolean actual) {
        this.timeUnix = timeUnix;
        this.date = date;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.description = description;
        this.provider = provider;
        this.daysBeforeActual = daysBeforeActual;
        this.actual = actual;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTimeUnix() {
        return timeUnix;
    }

    public void setTimeUnix(long timeUnix) {
        this.timeUnix = timeUnix;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public int getDaysBeforeActual() {
        return daysBeforeActual;
    }

    public void setDaysBeforeActual(int daysBeforeActual) {
        this.daysBeforeActual = daysBeforeActual;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "date='" + date + '\'' +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", pressure=" + pressure +
                ", clouds=" + clouds +
                ", windSpeed=" + windSpeed +
                ", description='" + description + '\'' +
                ", provider=" + provider +
                ", daysBeforeActual=" + daysBeforeActual +
                ", actual=" + actual +
                '}';
    }

    public String determineFieldByString(String item) {
        String value = "";
        item = item.toLowerCase();

        if (item.equals("date"))
            value = date;
        else if (item.equals("temp"))
            value = String.valueOf(Math.round((tempMin + tempMax) / 2));
        else if (item.equals("pressure"))
            value = String.valueOf(pressure);
        else if (item.equals("clouds"))
            value = String.valueOf(clouds);
        else if (item.equals("windspeed"))
            value = String.valueOf(windSpeed);
        else if (item.equals("description"))
            value = description;

        return value;
    }
}
