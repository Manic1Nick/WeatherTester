package ua.nick.weather.weatherFabric;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.model.Provider;
import ua.nick.weather.model.foreca.ForecaAll;
import ua.nick.weather.model.openWeather.OpenWeatherActual;
import ua.nick.weather.model.wunderground.wActual.WundergroundActual;
import ua.nick.weather.utils.ParseUtils;

import java.text.ParseException;

@Component
public class ActualWeatherFactory {

    private Gson gson;
    private ParseUtils parseUtils;

    public ActualWeatherFactory() {
        this.gson = new Gson();
        this.parseUtils = new ParseUtils();
    }

    public Forecast createActualModelFromJson(Provider provider, String json) throws ParseException {

        Forecast actual = null;

        if (provider == Provider.OPENWEATHER) {
            OpenWeatherActual openWeather = gson.fromJson(json, OpenWeatherActual.class);

            actual = parseUtils.makeActualWeatherFromOpenWeather(openWeather);

        } else if (provider == Provider.WUNDERGROUND) {
            WundergroundActual wundergroundActual = gson.fromJson(json, WundergroundActual.class);

            actual = parseUtils.makeActualWeatherFromWunderground(wundergroundActual);

        } else if (provider == Provider.FORECA) {
            ForecaAll forecaAll = gson.fromJson(json, ForecaAll.class);

            actual = parseUtils.makeActualWeatherFromForeca(forecaAll.getCc());
        }

        return actual;
    }
}
