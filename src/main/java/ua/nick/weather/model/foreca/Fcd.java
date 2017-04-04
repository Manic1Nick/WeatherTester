package ua.nick.weather.model.foreca;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fcd {

    @SerializedName("dt")
    @Expose
    private String dt;
    @SerializedName("tn")
    @Expose
    private Integer tn;
    @SerializedName("tx")
    @Expose
    private Integer tx;
    @SerializedName("s")
    @Expose
    private String s;
    @SerializedName("p")
    @Expose
    private Double p;
    @SerializedName("ws")
    @Expose
    private Integer ws;
    @SerializedName("wn")
    @Expose
    private String wn;
    @SerializedName("wd")
    @Expose
    private Integer wd;
    @SerializedName("rn")
    @Expose
    private Integer rn;
    @SerializedName("rx")
    @Expose
    private Integer rx;
    @SerializedName("px")
    @Expose
    private Integer px;
    @SerializedName("pn")
    @Expose
    private Integer pn;
    @SerializedName("uv")
    @Expose
    private Integer uv;
    @SerializedName("sr")
    @Expose
    private String sr;
    @SerializedName("ss")
    @Expose
    private String ss;
    @SerializedName("dl")
    @Expose
    private Integer dl;
    @SerializedName("mp")
    @Expose
    private Integer mp;
    @SerializedName("mr")
    @Expose
    private String mr;
    @SerializedName("ms")
    @Expose
    private String ms;
    @SerializedName("ca")
    @Expose
    private Integer ca;
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

    public Integer getTn() {
        return tn;
    }

    public void setTn(Integer tn) {
        this.tn = tn;
    }

    public Integer getTx() {
        return tx;
    }

    public void setTx(Integer tx) {
        this.tx = tx;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
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

    public Integer getRn() {
        return rn;
    }

    public void setRn(Integer rn) {
        this.rn = rn;
    }

    public Integer getRx() {
        return rx;
    }

    public void setRx(Integer rx) {
        this.rx = rx;
    }

    public Integer getPx() {
        return px;
    }

    public void setPx(Integer px) {
        this.px = px;
    }

    public Integer getPn() {
        return pn;
    }

    public void setPn(Integer pn) {
        this.pn = pn;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public Integer getDl() {
        return dl;
    }

    public void setDl(Integer dl) {
        this.dl = dl;
    }

    public Integer getMp() {
        return mp;
    }

    public void setMp(Integer mp) {
        this.mp = mp;
    }

    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public Integer getCa() {
        return ca;
    }

    public void setCa(Integer ca) {
        this.ca = ca;
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
