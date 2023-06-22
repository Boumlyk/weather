package com.test.metio.di;

import android.content.Context;

import com.test.metio.data.CookiesRepository;
import com.test.metio.module.cookies.Cookies;
import com.test.metio.tools.permissions.PermissionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    private Context context;

    public AppModule() {
    }


    @Provides
    @Singleton
    public Cookies provideCookies(@ApplicationContext Context appContext, CookiesRepository cookiesRepository) {
        return new Cookies(appContext, cookiesRepository);
    }

    @Provides
    @Singleton
    public PermissionManager providePermissionManager(@ApplicationContext Context appContext) {
        return new PermissionManager(appContext);
    }

}
