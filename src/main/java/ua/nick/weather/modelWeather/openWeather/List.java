package ua.nick.weather.modelWeather.openWeather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("temp")
    @Expose
    private ua.nick.weather.modelWeather.openWeather.Temp temp;
    @SerializedName("pressure")
    @Expose
    private Double pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("weather")
    @Expose
    private java.util.List<ua.nick.weather.modelWeather.openWeather.Weather> weather = null;
    @SerializedName("speed")
    @Expose
    private Double speed;
    @SerializedName("deg")
    @Expose
    private Integer deg;
    @SerializedName("clouds")
    @Expose
    private Integer clouds;
    @SerializedName("rain")
    @Expose
    private Double rain;
    @SerializedName("snow")
    @Expose
    private Double snow;

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public ua.nick.weather.modelWeather.openWeather.Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public java.util.List<ua.nick.weather.modelWeather.openWeather.Weather> getWeather() {
        return weather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    public Integer getClouds() {
        return clouds;
    }

    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public Double getSnow() {
        return snow;
    }

    public void setSnow(Double snow) {
        this.snow = snow;
    }

    @Override
    public String toString() {
        String date = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date((long) this.dt * 1000));

        return "List{" +
                "date=" + date +
                ", temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", weather=" + weather +
                ", speed=" + speed +
                ", deg=" + deg +
                ", clouds=" + clouds +
                ", rain=" + rain +
                ", snow=" + snow +
                '}';
    }
}
