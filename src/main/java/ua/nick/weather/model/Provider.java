package ua.nick.weather.model;

import java.util.Arrays;
import java.util.List;

public enum Provider {

    OPENWEATHER(
            "http://api.openweathermap.org/data/2.5/forecast/daily?q=Kiev,ua&units=metric&appid=e962ca86a64e342603c7da6c403847d6",
            "http://api.openweathermap.org/data/2.5/weather?q=Kiev,ua&units=metric&appid=e962ca86a64e342603c7da6c403847d6",
            "small_openweathermap_logo.png",
            "row_openweathermap_logo.png",
            "#ebea2f",
            7),

    WUNDERGROUND(
            "http://api.wunderground.com/api/8a173cdd18d0cabe/forecast/q/UA/Kiev.json",
            "http://api.wunderground.com/api/8a173cdd18d0cabe/conditions/q/UA/Kiev.json",
            "small_wu_logo.png",
            "row_wu_logo.png",
            "#9d9ad1",
            4),

    FORECA(
            "http://apitest.foreca.net/?lon=30.5234&lat=50.4501&key=IT7YtSoC0tgh3Chl0PHaZmb7g&format=json",
            "http://apitest.foreca.net/?lon=30.5234&lat=50.4501&key=IT7YtSoC0tgh3Chl0PHaZmb7g&format=json",
            "small_foreca_logo.png",
            "row_foreca_logo.png",
            "#29bfe4",
            10);
    //APIXU();

    //next colors: "#29e45c", "#e43e29", "#f19729", "#29f1d2"

    private String name = this.name();
    private int number = this.ordinal();

    //private static final Map<String, String> keyApiMap = Constants.PROVIDERS_KEYS_API_MAP;

    private String linkForecast;
    private String linkActual;
    private String logo;
    private String rowLogo;
    private String color;
    private int maxDaysForecast;

    Provider() {
    }

    Provider(String linkForecast, String linkActual, String logo, String rowLogo, String color, int maxDaysForecast) {
        this.linkForecast = linkForecast;
        this.linkActual = linkActual;
        this.logo = logo;
        this.rowLogo = rowLogo;
        this.color = color;
        this.maxDaysForecast = maxDaysForecast;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getLinkForecast() {
        return linkForecast;
    }

    public String getLinkActual() {
        return linkActual;
    }

    public String getLogo() {
        return logo;
    }

    public String getRowLogo() {
        return rowLogo;
    }

    public String getColor() {
        return color;
    }

    public int getMaxDaysForecast() {
        return maxDaysForecast;
    }

    public static List<Provider> getAll() {
        return Arrays.asList(values());
    }
}
