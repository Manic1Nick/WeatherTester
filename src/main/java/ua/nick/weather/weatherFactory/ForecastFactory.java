package ua.nick.weather.weatherFactory;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.model.Provider;
import ua.nick.weather.modelWeather.foreca.Fcd;
import ua.nick.weather.modelWeather.foreca.ForecaAll;
import ua.nick.weather.modelWeather.openWeather.OpenWeatherForecast;
import ua.nick.weather.modelWeather.wunderground.wForecast.Forecastday_;
import ua.nick.weather.modelWeather.wunderground.wForecast.WundergroundForecast;
import ua.nick.weather.utils.ParseUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ForecastFactory {

    private Gson gson;
    private ParseUtils parseUtils;

    public ForecastFactory() {
        this.gson = new Gson();
        this.parseUtils = new ParseUtils();
    }

    public List<Forecast> createListForecastModelsFromJson(Provider provider, String json)
            throws ParseException {

        List<Forecast> forecasts = new ArrayList<>();

        if (provider == Provider.OPENWEATHER) {
            OpenWeatherForecast openWeather = gson.fromJson(json, OpenWeatherForecast.class);

            List<ua.nick.weather.modelWeather.openWeather.List> forecastsList = openWeather.getList();
            for (ua.nick.weather.modelWeather.openWeather.List list : forecastsList) {
                Forecast forecast = parseUtils.makeForecastFromOpenWeather(list);
                forecast.setDaysBeforeActual(forecastsList.indexOf(list) + 1);
                forecasts.add(forecast);
            }

        } else if (provider == Provider.WUNDERGROUND) {
            WundergroundForecast wundergroundForecast = gson.fromJson(json, WundergroundForecast.class);

            List<Forecastday_> forecastsList = wundergroundForecast.getForecast().getSimpleforecast().getForecastday();
            for (Forecastday_ forecastday : forecastsList) {
                Forecast forecast = parseUtils.makeForecastFromWunderground(forecastday);
                forecast.setDaysBeforeActual(forecastsList.indexOf(forecastday) + 1);
                forecasts.add(forecast);
            }

        } else if (provider == Provider.FORECA) {
            ForecaAll forecaAll = gson.fromJson(json, ForecaAll.class);

            List<Fcd> fcdList = forecaAll.getFcd();
            for (Fcd fcd : fcdList) {
                Forecast forecast = parseUtils.makeForecastFromForeca(fcd);
                forecast.setDaysBeforeActual(fcdList.indexOf(fcd) + 1);
                forecasts.add(forecast);
            }
        }

        return forecasts;
    }
}
