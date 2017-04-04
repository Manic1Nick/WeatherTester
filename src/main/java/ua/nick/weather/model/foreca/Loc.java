package ua.nick.weather.model.foreca;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loc {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("tz")
    @Expose
    private String tz;
    @SerializedName("tzd")
    @Expose
    private String tzd;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("country")
    @Expose
    private String country;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    public String getTzd() {
        return tzd;
    }

    public void setTzd(String tzd) {
        this.tzd = tzd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
