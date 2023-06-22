package com.test.metio.ui.main.detailFragment;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.test.metio.data.WeatherRepository;
import com.test.metio.module.weather.CurrentWeatherEntity;
import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.BaseViewModel;
import com.test.metio.ui.adapter.CityWeatherAdapter;
import com.test.metio.ui.adapter.CityWeatherFiveDaysAdapter;
import com.test.metio.ui.main.homeFragment.FHome;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class FDetailsViewModel extends BaseViewModel {

    public MutableLiveData<CurrentWeatherEntity> currentWeather = new MutableLiveData<>();

    boolean isLoading = false;
    @Inject
    WeatherRepository weatherRepository;
    CityWeatherFiveDaysAdapter adapter;

    @Inject
    public FDetailsViewModel() {
    }

    @Override
    public void initiateViewModel(BaseActivity activity) {
        super.initiateViewModel(activity);
        currentWeather.setValue(cookies.getClickedEntity());
        getWeatherFiveDay();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragment.postValue(new FHome());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    public void onRefresh() {
        if (!isLoading){
            getWeatherFiveDay();
            weatherRepository.getCurrentWeather(Objects.requireNonNull(currentWeather.getValue()).getName(), false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(currentWeatherResponse -> {
                        currentWeather.postValue(currentWeatherResponse);
                        Log.e("TAG", "customerLocation: " + new Gson().toJson(currentWeatherResponse));
                    })
                    .doOnError(throwable -> {
                        Log.e("TAG", "customerLocation: " + throwable.getMessage());
                        isLoading = false;
                    }).subscribe();
        }

    }

    public void getWeatherFiveDay(){
        weatherRepository.getWeatherFiveDays(Objects.requireNonNull(currentWeather.getValue()).getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(list -> {
                    adapter.feedList(list);
                }).doOnError(throwable -> {

                }).subscribe();
    }
    public CityWeatherFiveDaysAdapter getAdapter(Context context){
        if (adapter==null)
            adapter=new CityWeatherFiveDaysAdapter(context,this);
        return adapter;
    }
}
