package ua.nick.weather.model;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    public static final Map<Provider, String> FORECASTS_PROVIDERS_MAP = new HashMap<Provider, String>()
    {{
        put(Provider.OPENWEATHER,
                "http://api.openweathermap.org/data/2.5/forecast/daily?q=Kiev,ua&units=metric&appid=e962ca86a64e342603c7da6c403847d6");
        //put(Provider.ACCUWEATHER,
                //"http://WeatherTester.accuweather.com/forecasts/v1/daily/5day/hoArfRosT1215.JSON?apikey=8aeadwqcjbfQSpGRN0mU4aBAI3d50OGC&language=en-us&details=true&metric=true");
        put(Provider.WUNDERGROUND,
                "http://api.wunderground.com/api/8a173cdd18d0cabe/forecast/q/UA/Kiev.json");
        put(Provider.FORECA,
                "http://apitest.foreca.net/?lon=30.5234&lat=50.4501&key=IT7YtSoC0tgh3Chl0PHaZmb7g&format=json");
        /*put(Provider.APIXU,
                "http://api.apixu.com/v1/forecast.json?key=03863e31405349848b9102935170104&q=Kiev");*/
    }};

    public static final Map<Provider, String> ACTUALS_PROVIDERS_MAP = new HashMap<Provider, String>()
    {{
        put(Provider.OPENWEATHER,
                "http://api.openweathermap.org/data/2.5/weather?q=Kiev,ua&units=metric&appid=e962ca86a64e342603c7da6c403847d6");
        //put(Provider.ACCUWEATHER,
                //"http://www.accuweather.com/en/us/state-college-pa/16801/current-weather/335315?lang=en-us&partner=8aeadwqcjbfQSpGRN0mU4aBAI3d50OGC");
        put(Provider.WUNDERGROUND,
                "http://api.wunderground.com/api/8a173cdd18d0cabe/conditions/q/UA/Kiev.json");
        put(Provider.FORECA,
                "http://apitest.foreca.net/?lon=30.5234&lat=50.4501&key=IT7YtSoC0tgh3Chl0PHaZmb7g&format=json");
        /*put(Provider.APIXU,
                "http://api.apixu.com/v1/current.json?key=03863e31405349848b9102935170104&q=Kiev");*/
    }};

    public static final Map<Provider, Integer> MAX_DAYS_FOR_FORECASTS_MAP = new HashMap<Provider, Integer>()
    {{
        put(Provider.OPENWEATHER, 7);
        put(Provider.WUNDERGROUND, 4);
        put(Provider.FORECA, 10);
    }};

    public static final Map<String, Integer> FIELDS_AND_RANGES_MAP = new HashMap<String, Integer>()
    {{
        put("Temp", 60); //from -30 to +30
        put("Pressure", 100);
        put("Clouds", 100); //from 0% to 100%
        put("WindSpeed", 10); //from 0 to 10 m/s
        put("Description", 0); //equals or not equals
    }};

    public static final Map<String, Double> FIELDS_AND_SHARES_MAP = new HashMap<String, Double>()
    {{
        put("Temp", 0.20);
        put("Pressure", 0.20);
        put("Clouds", 0.20);
        put("WindSpeed", 0.20);
        put("Description", 0.20);
        //total = 1.00
    }};
}