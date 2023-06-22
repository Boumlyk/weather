package com.test.metio.data.source.local.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.test.metio.module.cookies.Cookies;
import com.test.metio.module.weather.CurrentWeatherEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertOrUpdateCurrentWeather(CurrentWeatherEntity weather);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<List<Long>> insertOrUpdateCurrentWeathers(List<CurrentWeatherEntity> Documents);

    @Query("SELECT * FROM weather")
    Single<List<CurrentWeatherEntity>> getCurrentWeathers();

    @Query("DELETE FROM weather where id=:id")
    Single<Integer> delete(int id);

}