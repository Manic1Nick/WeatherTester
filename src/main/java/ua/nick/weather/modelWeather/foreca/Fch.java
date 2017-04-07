package ua.nick.weather.modelWeather.foreca;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fch {

    @SerializedName("dt")
    @Expose
    private String dt;
    @SerializedName("dtu")
    @Expose
    private String dtu;
    @SerializedName("t")
    @Expose
    private Integer t;
    @SerializedName("tf")
    @Expose
    private Integer tf;
    @SerializedName("s")
    @Expose
    private String s;
    @SerializedName("ws")
    @Expose
    private Integer ws;
    @SerializedName("wn")
    @Expose
    private String wn;
    @SerializedName("wd")
    @Expose
    private Integer wd;
    @SerializedName("p")
    @Expose
    private Double p;
    @SerializedName("dp")
    @Expose
    private Integer dp;
    @SerializedName("pr")
    @Expose
    private Integer pr;
    @SerializedName("rh")
    @Expose
    private Integer rh;
    @SerializedName("uv")
    @Expose
    private Integer uv;
    @SerializedName("v")
    @Expose
    private String v;
    @SerializedName("c")
    @Expose
    private Integer c;
    @SerializedName("pp")
    @Expose
    private Integer pp;
    @SerializedName("tp")
    @Expose
    private Integer tp;

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getDtu() {
        return dtu;
    }

    public void setDtu(String dtu) {
        this.dtu = dtu;
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

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Integer getDp() {
        return dp;
    }

    public void setDp(Integer dp) {
        this.dp = dp;
    }

    public Integer getPr() {
        return pr;
    }

    public void setPr(Integer pr) {
        this.pr = pr;
    }

    public Integer getRh() {
        return rh;
    }

    public void setRh(Integer rh) {
        this.rh = rh;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public Integer getC() {
        return c;
    }

    public void setC(Integer c) {
        this.c = c;
    }

    public Integer getPp() {
        return pp;
    }

    public void setPp(Integer pp) {
        this.pp = pp;
    }

    public Integer getTp() {
        return tp;
    }

    public void setTp(Integer tp) {
        this.tp = tp;
    }

}
