package ua.nick.weather.model.foreca;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecaAll {

    @SerializedName("cc")
    @Expose
    private Cc cc;
    @SerializedName("fcd")
    @Expose
    private List<Fcd> fcd = null;
    @SerializedName("fch")
    @Expose
    private List<Fch> fch = null;
    @SerializedName("loc")
    @Expose
    private Loc loc;
    @SerializedName("licensed_to")
    @Expose
    private String licensedTo;
    @SerializedName("hits")
    @Expose
    private Integer hits;
    @SerializedName("hit_limit")
    @Expose
    private Integer hitLimit;

    public Cc getCc() {
        return cc;
    }

    public void setCc(Cc cc) {
        this.cc = cc;
    }

    public List<Fcd> getFcd() {
        return fcd;
    }

    public void setFcd(List<Fcd> fcd) {
        this.fcd = fcd;
    }

    public List<Fch> getFch() {
        return fch;
    }

    public void setFch(List<Fch> fch) {
        this.fch = fch;
    }

    public Loc getLoc() {
        return loc;
    }

    public void setLoc(Loc loc) {
        this.loc = loc;
    }

    public String getLicensedTo() {
        return licensedTo;
    }

    public void setLicensedTo(String licensedTo) {
        this.licensedTo = licensedTo;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getHitLimit() {
        return hitLimit;
    }

    public void setHitLimit(Integer hitLimit) {
        this.hitLimit = hitLimit;
    }

}
