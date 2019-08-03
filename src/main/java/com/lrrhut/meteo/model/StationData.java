package com.lrrhut.meteo.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.ws.rs.QueryParam;
import java.time.LocalDateTime;

@Entity
public class StationData extends PanacheEntity {

    public static final String FIND_ALL_BY_DATE_DESC = "SELECT sd FROM StationData sd ORDER BY sd.dateutc DESC";

    @QueryParam("tempf")
    private Double tempf;

    @QueryParam("humidity")
    private Double humidity;

    @QueryParam("dewptf")
    private Double dewptf;

    @QueryParam("windchillf")
    private Double windchillf;

    @QueryParam("winddir")
    private Double winddir;

    @QueryParam("windspeedmph")
    private Double windspeedmph;

    @QueryParam("windgustmph")
    private Double windgustmph;

    @QueryParam("rainin")
    private Double rainin;

    @QueryParam("dailyrainin")
    private Double dailyrainin;

    @QueryParam("weeklyrainin")
    private Double weeklyrainin;

    @QueryParam("monthlyrainin")
    private Double monthlyrainin;

    @QueryParam("yearlyrainin")
    private Double yearlyrainin;

    @QueryParam("solarradiation")
    private Double solarradiation;

    @QueryParam("indoortempf")
    private Double indoortempf;

    @QueryParam("UV")
    private Double uv;

    @QueryParam("indoorhumidity")
    private Double indoorhumidity;

    @QueryParam("baromin")
    private Double baromin;

    @QueryParam("lowbatt")
    private Double lowbatt;

    private LocalDateTime dateutc;

    @QueryParam("softwaretype")
    private String softwaretype;

    @QueryParam("action")
    private String action;

    @QueryParam("realtime")
    private Long realtime;

    @QueryParam("rtfreq")
    private Long rtfreq;

    public Double getTempf() {
        return tempf;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getDewptf() {
        return dewptf;
    }

    public Double getWindchillf() {
        return windchillf;
    }

    public Double getWinddir() {
        return winddir;
    }

    public Double getWindspeedmph() {
        return windspeedmph;
    }

    public Double getWindgustmph() {
        return windgustmph;
    }

    public Double getRainin() {
        return rainin;
    }

    public Double getDailyrainin() {
        return dailyrainin;
    }

    public Double getWeeklyrainin() {
        return weeklyrainin;
    }

    public Double getMonthlyrainin() {
        return monthlyrainin;
    }

    public Double getYearlyrainin() {
        return yearlyrainin;
    }

    public Double getSolarradiation() {
        return solarradiation;
    }

    public Double getIndoortempf() {
        return indoortempf;
    }

    public Double getUv() {
        return uv;
    }

    public Double getIndoorhumidity() {
        return indoorhumidity;
    }

    public Double getBaromin() {
        return baromin;
    }

    public Double getLowbatt() {
        return lowbatt;
    }

    public LocalDateTime getDateutc() {
        return dateutc;
    }

    public void setDateutc(LocalDateTime dateutc) {
        this.dateutc = dateutc;
    }

    public String getSoftwaretype() {
        return softwaretype;
    }

    public String getAction() {
        return action;
    }

    public Long getRealtime() {
        return realtime;
    }

    public Long getRtfreq() {
        return rtfreq;
    }
}
