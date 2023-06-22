package com.test.metio.data.source.remote.utils.interceptor;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.test.metio.module.cookies.Cookies;
import com.test.metio.tools.security.AuthenticityChecker;
import com.test.metio.ui.splash.SplashActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class RequestInterceptor implements Interceptor {

    Cookies cookies;
    Context context;

    @Inject
    public RequestInterceptor(Context context, Cookies cookies) {
        this.cookies = cookies;
        this.context = context;
    }

    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (!AuthenticityChecker.checkAuthenticity(context)) { //
            new Handler(Looper.getMainLooper()).post(this::logOut);
        } else {
            Request originalRequest = chain.request();
            Map<String, String> request_headers = getRequestHeaders(originalRequest);
            Request.Builder builder = originalRequest.newBuilder();
            for (Map.Entry<String, String> entry : request_headers.entrySet())
                builder.header(entry.getKey(), entry.getValue());

            HttpUrl.Builder url_builder = originalRequest.url().newBuilder();
            Request request = builder.url(url_builder.build()).build();
            return chain.proceed(request);
        }
        return null;
    }
    private Map<String, String> getRequestHeaders(Request originalRequest) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        return headers;

    }
    public void logOut() {
        Intent myIntent = new Intent(context, SplashActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(myIntent);
    }


}
