package com.test.metio.di;


import android.content.Context;

import com.google.gson.Gson;
import com.test.metio.BuildConfig;
import com.test.metio.data.source.remote.APISettings;
import com.test.metio.data.source.remote.service.WeatherService;
import com.test.metio.data.source.remote.utils.interceptor.RequestInterceptor;
import com.test.metio.data.source.remote.utils.interceptor.ResponseInterceptor;
import com.test.metio.module.cookies.Cookies;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RemoteModule {
    @Provides
    @Singleton
    public Retrofit provideRetrofit(Cookies cookies, @ApplicationContext Context context) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(APISettings.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(APISettings.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(APISettings.WRITE_TIMEOUT, TimeUnit.SECONDS);

        httpClientBuilder.interceptors().clear();
        httpClientBuilder.addInterceptor(new RequestInterceptor(context, cookies));
        if (BuildConfig.DEBUG)
            httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        httpClientBuilder.addInterceptor(new ResponseInterceptor(context, cookies));

        httpClientBuilder.followRedirects(false);
        httpClientBuilder.followSslRedirects(false);
        OkHttpClient okHttpClient = httpClientBuilder.build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BACKEND_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()));

        return retrofitBuilder.client(okHttpClient).build();

    }

    @Provides
    @Singleton
    public WeatherService provideDocumentService(Retrofit retrofit) {
        return retrofit.create(WeatherService.class);
    }


}
