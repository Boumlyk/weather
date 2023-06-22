package com.test.metio.data.source.remote.utils.interceptor;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.test.metio.module.cookies.Cookies;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class ResponseInterceptor implements Interceptor {

    Context context;
    Cookies cookies;

    @Inject
    public ResponseInterceptor(@ApplicationContext Context context, Cookies cookies) {
        this.context = context;
        this.cookies = cookies;
    }

    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        try {
            Request request = chain.request();
            Response response = chain.proceed(request);


            if (response.isSuccessful())
                return response;
        } catch (Exception e) {
            Log.e("TAG", "intercept: "+e.getMessage());
        }
        throw new RuntimeException("not found");
    }




}
