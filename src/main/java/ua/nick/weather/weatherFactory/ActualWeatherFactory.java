package ua.nick.weather.weatherFactory;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.model.Provider;
import ua.nick.weather.modelWeather.darkSky.DarkSky;
import ua.nick.weather.modelWeather.foreca.ForecaAll;
import ua.nick.weather.modelWeather.openWeather.OpenWeatherActual;
import ua.nick.weather.modelWeather.wunderground.wActual.WundergroundActual;
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

        } else if (provider == Provider.DARK_SKY) {
            DarkSky darkSky = gson.fromJson(json, DarkSky.class);

            actual = parseUtils.makeActualWeatherFromDarkSky(darkSky.getCurrently());
        }

        return actual;
    }
}
