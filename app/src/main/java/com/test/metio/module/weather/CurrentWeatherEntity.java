package com.test.metio.module.weather;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "weather")
public class CurrentWeatherEntity {
    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("name")
    private String name;

    @SerializedName("dt")
    private long dt;

    private String weatherStatus;

    private String weatherStatusMain;
    @SerializedName("temp")
    private double temp;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("country")
    private String country;

    @SerializedName("speed")
    private double speed;

    @SerializedName("deg")
    private double deg;

    @SerializedName("lon")
    private double lon;

    @SerializedName("lat")
    private double lat;

    @SerializedName("temp_min")
    private double tempMin;

    @SerializedName("temp_max")
    private double tempMax;

    @SerializedName("icon")
    private String icon;

    @SerializedName("pressure")
    private double pressure;

    @SerializedName("dt_txt")
    private String dtTxt;

    public CurrentWeatherEntity() {
    }

    public CurrentWeatherEntity(int id,long dt,String dtTxt, String name,String icon,double pressure,
                                String weatherStatus, double temp,
                                int humidity, String country, double speed,
                                double deg, double lon, double lat,String weatherStatusMain,
                                double tempMin,double tempMax) {
        this.id = id;
        this.dt=dt;
        this.dtTxt=dtTxt;
        this.name = name;
        this.icon=icon;
        this.pressure=pressure;
        this.weatherStatus = weatherStatus;
        this.temp = temp;
        this.humidity = humidity;
        this.country = country;
        this.speed = speed;
        this.deg = deg;
        this.lon = lon;
        this.lat = lat;
        this.weatherStatusMain=weatherStatusMain;
        this.tempMin=tempMin;
        this.tempMax=tempMax;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

    public double getTempMin() {
        return tempMin;
    }

    public long getDt() {
        return dt;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public String getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(String weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getWeatherStatusMain() {
        return weatherStatusMain;
    }

    public void setWeatherStatusMain(String weatherStatusMain) {
        this.weatherStatusMain = weatherStatusMain;
    }
}
