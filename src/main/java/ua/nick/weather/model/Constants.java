package ua.nick.weather.model;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    public static final Map<String, String> PROVIDERS_KEYS_API_MAP = new HashMap<String, String>()
    {{
        put("OPENWEATHER", "e962ca86a64e342603c7da6c403847d6");
        //put("ACCUWEATHER", "8aeadwqcjbfQSpGRN0mU4aBAI3d50OGC");
        put("WUNDERGROUND", "8a173cdd18d0cabe");
        put("FORECA", "IT7YtSoC0tgh3Chl0PHaZmb7g");
        /*put("APIXU", "03863e31405349848b9102935170104");*/
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