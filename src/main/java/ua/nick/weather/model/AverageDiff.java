package ua.nick.weather.model;

import javax.persistence.*;

@Entity
@Table(name = "averages")
public class AverageDiff {

    private Long id;
    private Provider provider;
    private int days;
    private double value;

    public AverageDiff() {
    }

    public AverageDiff(Provider provider) {
        this.provider = provider;
    }

    public AverageDiff(Diff diff) {
        this.provider = diff.getProvider();
        this.days = 1;
        this.value = diff.getAverageDayDiff();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
