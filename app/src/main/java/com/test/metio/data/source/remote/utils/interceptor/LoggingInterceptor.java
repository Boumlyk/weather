package com.test.metio.data.source.remote.utils.interceptor;


import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class LoggingInterceptor implements Interceptor {

    @Inject
    public LoggingInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.d("LoggingInterceptor", String.format("Intercepted: URL: %s\n headers: %s \n: body: %s \n", request.headers(), request.body(), request.url()));
        return chain.proceed(request);
    }
}
