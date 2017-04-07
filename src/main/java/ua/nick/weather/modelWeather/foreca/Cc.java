package ua.nick.weather.modelWeather.foreca;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cc {

    @SerializedName("dt")
    @Expose
    private String dt;
    @SerializedName("station")
    @Expose
    private String station;
    @SerializedName("t")
    @Expose
    private Integer t;
    @SerializedName("tf")
    @Expose
    private Integer tf;
    @SerializedName("p")
    @Expose
    private Double p;
    @SerializedName("s")
    @Expose
    private String s;
    @SerializedName("ws")
    @Expose
    private Integer ws;
    @SerializedName("uv")
    @Expose
    private Integer uv;
    @SerializedName("wn")
    @Expose
    private String wn;
    @SerializedName("wd")
    @Expose
    private Integer wd;
    @SerializedName("rh")
    @Expose
    private Integer rh;
    @SerializedName("pr")
    @Expose
    private Integer pr;
    @SerializedName("v")
    @Expose
    private Integer v;
    @SerializedName("dp")
    @Expose
    private Integer dp;
    @SerializedName("pw")
    @Expose
    private Integer pw;
    @SerializedName("c")
    @Expose
    private Integer c;
    @SerializedName("ds")
    @Expose
    private Integer ds;
    @SerializedName("pt")
    @Expose
    private Integer pt;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public Integer getTf() {
        return tf;
    }

    public void setTf(Integer tf) {
        this.tf = tf;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Integer getWs() {
        return ws;
    }

    public void setWs(Integer ws) {
        this.ws = ws;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public String getWn() {
        return wn;
    }

    public void setWn(String wn) {
        this.wn = wn;
    }

    public Integer getWd() {
        return wd;
    }

    public void setWd(Integer wd) {
        this.wd = wd;
    }

    public Integer getRh() {
        return rh;
    }

    public void setRh(Integer rh) {
        this.rh = rh;
    }

    public Integer getPr() {
        return pr;
    }

    public void setPr(Integer pr) {
        this.pr = pr;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Integer getDp() {
        return dp;
    }

    public void setDp(Integer dp) {
        this.dp = dp;
    }

    public Integer getPw() {
        return pw;
    }

    public void setPw(Integer pw) {
        this.pw = pw;
    }

    public Integer getC() {
        return c;
    }

    public void setC(Integer c) {
        this.c = c;
    }

    public Integer getDs() {
        return ds;
    }

    public void setDs(Integer ds) {
        this.ds = ds;
    }

    public Integer getPt() {
        return pt;
    }

    public void setPt(Integer pt) {
        this.pt = pt;
    }

}
