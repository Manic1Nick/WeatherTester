
package ua.nick.weather.modelWeather.apixu.aForecast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Apixu {

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("current")
    @Expose
    private ua.nick.weather.modelWeather.apixu.aForecast.Current current;
    @SerializedName("forecast")
    @Expose
    private Forecast forecast;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ua.nick.weather.modelWeather.apixu.aForecast.Current getCurrent() {
        return current;
    }

    public void setCurrent(ua.nick.weather.modelWeather.apixu.aForecast.Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

}
