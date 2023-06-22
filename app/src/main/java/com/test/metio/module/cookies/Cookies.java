package com.test.metio.module.cookies;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.test.metio.data.CookiesRepository;
import com.test.metio.module.weather.CurrentWeatherEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@Singleton
@Entity(tableName = "myCookies")
public class Cookies{

    @SerializedName("ID")
    public static String ID = "UNIQUE_ID";
    @NonNull
    @PrimaryKey
    @SerializedName("cookiesId")
    String cookiesId = ID;

    @Ignore
    boolean isBackupFinished=false;

    String language="en";
    public static final String METRICS = "metric";

    private List<String> listOfCity=new ArrayList<>();

    @Ignore
    private long downloadedTime=0;

    @Ignore
    private CurrentWeatherEntity clickedEntity;



    public Cookies() {
    }

    @Inject
    @SuppressLint("HardwareIds")
    public Cookies(@ApplicationContext Context context, CookiesRepository cookiesRepository) {
        cookiesRepository.getCookies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(mCookies -> {
                    if ((mCookies != null) && (!isBackupFinished)) {
                        update(mCookies);
                        isBackupFinished = true;
                    }
                }).doOnError(throwable -> {
                    Log.e("CookiesTAG", "Cookies: " +throwable);
                })
                .subscribe();
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////////////// Getters & Setters /////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public void update(Cookies other) {
        this.listOfCity=other.listOfCity;
    }

    public CurrentWeatherEntity getClickedEntity() {
        return clickedEntity;
    }

    public void setClickedEntity(CurrentWeatherEntity clickedEntity) {
        this.clickedEntity = clickedEntity;
    }

    @NonNull
    public String getCookiesId() {
        return cookiesId;
    }

    public void setCookiesId(@NonNull String cookiesId) {
        this.cookiesId = cookiesId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public long getDownloadedTime() {
        return downloadedTime;
    }

    public void setDownloadedTime(long downloadedTime) {
        this.downloadedTime = downloadedTime;
    }

    public boolean isDownloadRecently(){
        return downloadedTime >= System.currentTimeMillis();
    }

    public List<String> getListOfCity() {
        if (listOfCity.isEmpty()){
            listOfCity.add("Casablanca");
            listOfCity.add("Rabat");
            listOfCity.add("Marrakech");
            listOfCity.add("Tangier");
            listOfCity.add("Fes");
        }
        return listOfCity;
    }

    public void setListOfCity(List<String> listOfCity) {
        this.listOfCity = listOfCity;
    }


}