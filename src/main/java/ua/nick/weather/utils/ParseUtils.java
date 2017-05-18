package ua.nick.weather.utils;

import com.google.gson.Gson;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.model.Provider;
import ua.nick.weather.modelWeather.darkSky.Currently;
import ua.nick.weather.modelWeather.darkSky.DarkSky;
import ua.nick.weather.modelWeather.darkSky.Datum_;
import ua.nick.weather.modelWeather.foreca.Cc;
import ua.nick.weather.modelWeather.foreca.Fcd;
import ua.nick.weather.modelWeather.foreca.ForecaAll;
import ua.nick.weather.modelWeather.openWeather.OpenWeatherActual;
import ua.nick.weather.modelWeather.openWeather.OpenWeatherForecast;
import ua.nick.weather.modelWeather.wunderground.wActual.WundergroundActual;
import ua.nick.weather.modelWeather.wunderground.wForecast.Forecastday_;
import ua.nick.weather.modelWeather.wunderground.wForecast.WundergroundForecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseUtils {

    private Gson gson;

    public ParseUtils() {
        gson = new Gson();
    }

    //OPEN WEATHER
    public List<Forecast> parseForecastsFromOpenWeather(List<Forecast> forecasts, String json) {
        OpenWeatherForecast openWeather = gson.fromJson(json, OpenWeatherForecast.class);

        List<ua.nick.weather.modelWeather.openWeather.List> forecastsList = openWeather.getList();
        for (ua.nick.weather.modelWeather.openWeather.List list : forecastsList) {
            Forecast forecast = createForecastFromOpenWeather(list);
            forecast.setDaysBeforeActual(forecastsList.indexOf(list) + 1);
            forecasts.add(forecast);
        }
        return forecasts;
    }

    public Forecast parseActualWeatherFromOpenWeather(Forecast actual, String json) throws ParseException {
        OpenWeatherActual openWeather = gson.fromJson(json, OpenWeatherActual.class);

        Long epoc = (long) openWeather.getDt();
        String date = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date(epoc * 1000));
        actual.setTimeUnix(epoc);
        actual.setDate(date);
        actual.setTempMin(Math.round(openWeather.getMain().getTempMin()));
        actual.setTempMax(Math.round(openWeather.getMain().getTempMax()));
        actual.setPressure(Math.round(openWeather.getMain().getPressure()));
        actual.setClouds(openWeather.getClouds().getAll());
        actual.setWindSpeed(Math.round(openWeather.getWind().getSpeed()));
        actual.setDescription(openWeather.getWeather().get(0).getMain());

        return actual;
    }

    //WEATHER UNDERGROUND
    public List<Forecast> parseForecastsFromWunderground(List<Forecast> forecasts, String json) {
        WundergroundForecast wundergroundForecast = gson.fromJson(json, WundergroundForecast.class);

        List<Forecastday_> forecastsList = wundergroundForecast.getForecast().getSimpleforecast().getForecastday();
        for (Forecastday_ forecastday : forecastsList) {
            Forecast forecast = createForecastFromWunderground(forecastday);
            forecast.setDaysBeforeActual(forecastsList.indexOf(forecastday) + 1);
            forecasts.add(forecast);
        }
        return forecasts;
    }

    public Forecast parseActualWeatherFromWunderground(Forecast actual, String json) throws ParseException {
        WundergroundActual wunderground = gson.fromJson(json, WundergroundActual.class);

        Long epoc = Long.parseLong(wunderground.getCurrentObservation().getObservationEpoch());
        String date = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date(epoc * 1000));
        actual.setTimeUnix(epoc);
        actual.setDate(date);
        actual.setTempMin((int) Math.round(wunderground.getCurrentObservation().getTempC()));
        actual.setTempMax((int) Math.round(wunderground.getCurrentObservation().getTempC()));
        actual.setPressure((int) Math.round(Double.parseDouble(wunderground.getCurrentObservation().getPressureMb())));
        actual.setClouds(Integer.parseInt(wunderground.getCurrentObservation().getRelativeHumidity().split("%")[0]));
        actual.setWindSpeed((int) Math.round(wunderground.getCurrentObservation().getWindMph() * 0.44704));
        actual.setDescription(wunderground.getCurrentObservation().getWeather());

        return actual;
    }


    //FORECA
    public List<Forecast> parseForecastsFromForeca(List<Forecast> forecasts, String json) throws ParseException {
        ForecaAll forecaAll = gson.fromJson(json, ForecaAll.class);

        List<Fcd> fcdList = forecaAll.getFcd();
        for (Fcd fcd : fcdList) {
            Forecast forecast = createForecastFromForeca(fcd);
            forecast.setDaysBeforeActual(fcdList.indexOf(fcd) + 1);
            forecasts.add(forecast);
        }
        return forecasts;
    }

    public Forecast parseActualWeatherFromForeca(Forecast actual, String json) throws ParseException {
        ForecaAll forecaAll = gson.fromJson(json, ForecaAll.class);
        Cc cc = forecaAll.getCc();

        String date = cc.getDt().substring(0, 10).replace("-", "/");
        long epoch = new SimpleDateFormat("yyyy/MM/dd").parse(date).getTime();
        actual.setTimeUnix(epoch / 1000);
        actual.setDate(date);
        actual.setTempMin(cc.getT());
        actual.setTempMax(cc.getT());
        actual.setPressure(cc.getPr());
        actual.setClouds(cc.getRh());
        actual.setWindSpeed(cc.getWs());
        actual.setDescription(createDescriptionForForeca(cc.getS())); //1 letter + 3 numbers

        return actual;
    }

    //DARK_SKY
    public List<Forecast> parseForecastsFromDarkSky(List<Forecast> forecasts, String json) throws ParseException {
        DarkSky darkSky = gson.fromJson(json, DarkSky.class);

        List<Datum_> datumList = darkSky.getDaily().getData();
        for (Datum_ datum : datumList) {
            Forecast forecast = createForecastFromDarkSky(datum);
            forecast.setDaysBeforeActual(datumList.indexOf(datum) + 1);
            forecasts.add(forecast);
        }
        return forecasts;
    }

    public Forecast parseActualWeatherFromDarkSky(Forecast actual, String json) throws ParseException {
        DarkSky darkSky = gson.fromJson(json, DarkSky.class);
        Currently currently = darkSky.getCurrently();

        Long epoch = currently.getTime().longValue();
        String date = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date(epoch * 1000));
        actual.setTimeUnix(epoch / 1000);
        actual.setDate(date);
        actual.setTempMin((int) Math.round((currently.getTemperature() - 32.0) * (5.0/9.0)));
        actual.setTempMax((int) Math.round((currently.getTemperature() - 32.0) * (5.0/9.0)));
        actual.setPressure((int) Math.round(currently.getPressure()));
        actual.setClouds((int) Math.round(currently.getCloudCover() * 100));
        actual.setWindSpeed((int) Math.round(currently.getWindSpeed()));
        actual.setDescription(currently.getSummary());

        return actual;
    }

    private Forecast createForecastFromOpenWeather(ua.nick.weather.modelWeather.openWeather.List list) {
        Forecast forecast = new Forecast(Provider.OPENWEATHER, false);

        Long epoc = (long) list.getDt();
        String date = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date(epoc * 1000));
        forecast.setTimeUnix(epoc);
        forecast.setDate(date);
        forecast.setTempMin((int) Math.round(list.getTemp().getMin()));
        forecast.setTempMax((int) Math.round(list.getTemp().getMax()));
        forecast.setPressure((int) Math.round(list.getPressure()));
        forecast.setClouds(list.getClouds());
        forecast.setWindSpeed((int) Math.round(list.getSpeed()));
        forecast.setDescription(list.getWeather().get(0).getMain());

        return forecast;
    }

    private Forecast createForecastFromWunderground(Forecastday_ forecastday) {
        Forecast forecast = new Forecast(Provider.WUNDERGROUND, false);

        Long epoc = Long.parseLong(forecastday.getDate().getEpoch());
        String date = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date(epoc * 1000));
        forecast.setTimeUnix(epoc);
        forecast.setDate(date);
        forecast.setTempMin(Integer.valueOf(forecastday.getLow().getCelsius()));
        forecast.setTempMax(Integer.valueOf(forecastday.getHigh().getCelsius()));
        forecast.setPressure(1000);//no data in json
        forecast.setClouds(forecastday.getAvehumidity());
        forecast.setWindSpeed(forecastday.getAvewind().getMph());
        forecast.setDescription(forecastday.getConditions());

        return forecast;
    }

    private Forecast createForecastFromForeca(Fcd fcd) throws ParseException {
        Forecast forecast = new Forecast(Provider.FORECA, false);

        long epoch = new SimpleDateFormat("yyyy-MM-dd").parse(fcd.getDt()).getTime();
        String date = fcd.getDt().replace("-", "/");
        forecast.setTimeUnix(epoch / 1000);
        forecast.setDate(date);
        forecast.setTempMin(fcd.getTn());
        forecast.setTempMax(fcd.getTx());
        forecast.setPressure((fcd.getPx() + fcd.getPn()) / 2);
        forecast.setClouds((fcd.getRx() + fcd.getRn()) / 2);
        forecast.setWindSpeed(fcd.getWs());
        forecast.setDescription(createDescriptionForForeca(fcd.getS()));//1 letter + 3 numbers

        return forecast;
    }

    private Forecast createForecastFromDarkSky(Datum_ datum) throws ParseException {
        Forecast forecast = new Forecast(Provider.DARK_SKY, false);

        Long epoch = datum.getTime().longValue();
        String date = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date(epoch * 1000));
        forecast.setTimeUnix(epoch / 1000);
        forecast.setDate(date);
        forecast.setTempMin((int) Math.round((datum.getTemperatureMin() - 32.0) * (5.0/9.0))); //[°C] = ([°F] − 32) ×  5⁄9
        forecast.setTempMax((int) Math.round((datum.getTemperatureMax() - 32.0) * (5.0/9.0)));
        forecast.setPressure((int) Math.round(datum.getPressure()));
        forecast.setClouds((int) Math.round(datum.getCloudCover() * 100));
        forecast.setWindSpeed((int) Math.round(datum.getWindSpeed()));
        forecast.setDescription(datum.getSummary());

        return forecast;
    }

    private String createDescriptionForForeca(String code) { //example = d000

        Map<String, String> cloudiness = new HashMap<>();
        cloudiness.put("0", "clear");
        cloudiness.put("1", "almost clear");
        cloudiness.put("2", "half cloudy");
        cloudiness.put("3", "broken");
        cloudiness.put("4", "overcast");
        cloudiness.put("5", "thin high clouds");
        cloudiness.put("6", "fog");

        Map<String, String> precipitationRate = new HashMap<>();
        precipitationRate.put("0", "no precipitation");
        precipitationRate.put("1", "slight precipitation");
        precipitationRate.put("2", "showers");
        precipitationRate.put("3", "precipitation");
        precipitationRate.put("4", "thunder");

        Map<String, String> precipitation = new HashMap<>();
        precipitation.put("0", "rain");
        precipitation.put("1", "sleet");
        precipitation.put("2", "snow");

        return cloudiness.get(String.valueOf(code.charAt(1))) + " " +
                precipitationRate.get(String.valueOf(code.charAt(2))) + " " +
                precipitation.get(String.valueOf(code.charAt(3)));
    }
}