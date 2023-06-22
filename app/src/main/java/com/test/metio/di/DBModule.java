package com.test.metio.di;

import android.content.Context;

import com.test.metio.data.source.local.db.LocalDatabase;
import com.test.metio.data.source.local.db.dao.CookiesDao;
import com.test.metio.data.source.local.db.dao.WeatherDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DBModule {
    @Provides
    @Singleton
    public static LocalDatabase getLocalDatabase(@ApplicationContext Context context) {
        return LocalDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static CookiesDao getCookiesDao(LocalDatabase database) {
        return database.cookiesDao();
    }

    @Provides
    @Singleton
    public static WeatherDao getWeatherDao(LocalDatabase database) {
        return database.weatherDao();
    }


}
