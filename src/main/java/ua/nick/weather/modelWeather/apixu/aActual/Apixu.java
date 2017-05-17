
package ua.nick.weather.modelWeather.apixu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ua.nick.weather.modelWeather.apixu.aForecast.Location;

public class Apixu {

    @SerializedName("location")
    @Expose
    private ua.nick.weather.modelWeather.apixu.aForecast.Location location;
    @SerializedName("current")
    @Expose
    private ua.nick.weather.modelWeather.apixu.aForecast.Current current;

    public ua.nick.weather.modelWeather.apixu.aForecast.Location getLocation() {
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

}
