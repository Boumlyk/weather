package com.test.metio.data;


import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.metio.BuildConfig;
import com.test.metio.data.source.local.db.dao.WeatherDao;
import com.test.metio.data.source.remote.service.WeatherService;
import com.test.metio.module.cookies.Cookies;
import com.test.metio.module.weather.CurrentWeatherEntity;
import com.test.metio.module.weather.CurrentWeatherResponse;
import com.test.metio.tools.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;


@Singleton
public class WeatherRepository {

    private final WeatherService weatherService;
    private final WeatherDao weatherDao;
    @Inject
    Cookies cookies;

    @Inject
    public WeatherRepository(WeatherService weatherService, WeatherDao weatherDao) {
        this.weatherService = weatherService;
        this.weatherDao = weatherDao;
    }

    @SuppressLint("CheckResult")
    public Single<CurrentWeatherEntity> getCurrentWeather(String cityName, boolean shouldStored) {
        if (cityName != null)
            return weatherService.getCurrentWeather(cityName, Cookies.METRICS, cookies.getLanguage(),
                    BuildConfig.API_KEY).flatMap(response -> {
                if (shouldStored && response.getName() != null) {
                    weatherDao.insertOrUpdateCurrentWeather(Utils.getCurrentEntity(response)).blockingGet();
                }
                if (response.getName() != null)
                    return Single.just(Utils.getCurrentEntity(response));
                else
                    return Single.error(new Exception("city not found"));
            });
        else
            return Single.error(new Exception("city name is null"));
    }

    public Single<Boolean> getWeatherForCities(List<String> citiesName) {
        if (cookies.isDownloadRecently())
            return Single.just(true);
        else {
            return Observable.fromIterable(citiesName)
                    .flatMap(cityName -> {
                        if (cityName != null)
                            return getCurrentWeather(cityName, false).toObservable();
                        else {
                            return null;
                        }
                    })
                    .toList()
                    .flatMap(results -> {
                        return weatherDao.insertOrUpdateCurrentWeathers(results).flatMap(longs -> {
                            cookies.setDownloadedTime(System.currentTimeMillis() + 600000);
                            return Single.just(longs.size() == results.size());
                        });
                    });
        }
    }

    public Single<List<CurrentWeatherEntity>> getWeatherFiveDays(String cityName) {
        if (cityName != null)
            return weatherService.getDaysWeather(cityName, Cookies.METRICS, cookies.getLanguage(),
                    BuildConfig.API_KEY).flatMap(response -> {
                if (response.has("list")) {
                    List<CurrentWeatherResponse> responses = new Gson().fromJson(response.get("list"), new TypeToken<List<CurrentWeatherResponse>>() {
                    }.getType());
                    return Single.just(Utils.getCurrentEntities(responses));
                }
                return Single.just(new ArrayList<>());
            });
        else
            return Single.error(new Exception("city name is null"));
    }

    public Single<List<CurrentWeatherEntity>> getListOfWeather() {
        return getWeatherForCities(cookies.getListOfCity()).flatMap(aBoolean -> {
            if (aBoolean) {
                return weatherDao.getCurrentWeathers().flatMap(Single::just);
            } else {
                return Single.just(new ArrayList<>());
            }
        });
    }

    public Single<Boolean> deleteItem(CurrentWeatherEntity entity) {
        return weatherDao.delete(entity.getId()).flatMap(integer -> {
            return Single.just(integer > 0);
        });
    }
}
