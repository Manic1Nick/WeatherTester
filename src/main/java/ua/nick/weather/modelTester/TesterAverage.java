package ua.nick.weather.modelTester;

import ua.nick.weather.model.Provider;

public class TesterAverage {

    private Provider provider;
    private String date;
    private String valueDay;
    private String valueTotal;
    private String valueDays;

    public TesterAverage() {
    }

    public TesterAverage(Provider provider, String date, String valueDay, String valueTotal, String valueDays) {
        this.provider = provider;
        this.date = date;
        this.valueDay = valueDay;
        this.valueTotal = valueTotal;
        this.valueDays = valueDays;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValueDay() {
        return valueDay;
    }

    public void setValueDay(String valueDay) {
        this.valueDay = valueDay;
    }

    public String getValueTotal() {
        return valueTotal;
    }

    public void setValueTotal(String valueTotal) {
        this.valueTotal = valueTotal;
    }

    public String getValueDays() {
        return valueDays;
    }

    public void setValueDays(String valueDays) {
        this.valueDays = valueDays;
    }
}
