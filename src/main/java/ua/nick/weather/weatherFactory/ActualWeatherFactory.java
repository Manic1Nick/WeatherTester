package ua.nick.weather.weatherFactory;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.model.Provider;
import ua.nick.weather.utils.ParseUtils;

import java.text.ParseException;

@Component
public class ActualWeatherFactory {

    private Gson gson;
    private ParseUtils parseUtils;

    public ActualWeatherFactory() {
        this.parseUtils = new ParseUtils();
    }

    public Forecast createActualModelFromJson(Provider provider, String json) throws ParseException {

        Forecast actual = new Forecast(provider, true);

        if (Provider.OPENWEATHER == provider) {
            actual = parseUtils.parseActualWeatherFromOpenWeather(actual, json);

        } else if (Provider.WUNDERGROUND == provider) {
            actual = parseUtils.parseActualWeatherFromWunderground(actual, json);

        } else if (Provider.FORECA == provider) {
            actual = parseUtils.parseActualWeatherFromForeca(actual, json);

        } else if (Provider.DARK_SKY == provider) {
            actual = parseUtils.parseActualWeatherFromDarkSky(actual, json);
        }

        return actual;
    }
}