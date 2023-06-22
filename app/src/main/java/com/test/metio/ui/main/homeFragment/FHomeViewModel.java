package com.test.metio.ui.main.homeFragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.test.metio.R;
import com.test.metio.data.WeatherRepository;
import com.test.metio.module.weather.CurrentWeatherEntity;
import com.test.metio.tools.location.LocationManager;
import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.BaseViewModel;
import com.test.metio.ui.adapter.CityWeatherAdapter;
import com.test.metio.ui.main.detailFragment.FDetails;
import com.test.metio.ui.main.moreFragment.FMore;

import java.util.Collections;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class FHomeViewModel extends BaseViewModel {
    CityWeatherAdapter adapter;
    @Inject
    WeatherRepository weatherRepository;
    MutableLiveData<Location> customerLocation=new MutableLiveData<>();
    public MutableLiveData<CurrentWeatherEntity> currentWeatherMutableLiveData=new MutableLiveData<>();
    @Inject
    LocationManager locationManager;
    boolean isLoading=false;
    @Inject
    public FHomeViewModel() {
    }
    Activity activity;

    @Override
    public void initiateViewModel(BaseActivity activity) {
        super.initiateViewModel(activity);
        this.activity=activity;
        locationManager.requestLastLocation(activity,customerLocation);
        getWeatherForMyLocation(activity);
        getWeather();
    }

    private void getWeather() {
        actions.postValue(Collections.singletonList(BaseActivity.ACTION_START_LOADING));
        weatherRepository.getListOfWeather().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(currentWeatherResponse -> {
                    if (!currentWeatherResponse.isEmpty())
                        adapter.feedList(currentWeatherResponse);
                })
                .doOnError(throwable -> {
                    Log.e("TAG", "initiateViewModel: "+throwable.getMessage() );
                }).doFinally(() -> {
                    actions.postValue(Collections.singletonList(BaseActivity.ACTION_FINISH_LOADING));
                }).subscribe();
    }

    public void onRefresh(BaseActivity activity){
        getWeather();
        getWeatherForMyLocation(activity);
    }

    private void getWeatherForMyLocation(BaseActivity activity) {
        customerLocation.observe(activity,location -> {
            if (location!=null && !isLoading){
                Log.e("TAG", "customerLocation: "+locationManager.getCityName(activity,
                        location.getLatitude(),location.getLongitude()) +location.getLongitude()+" --->" +location.getLatitude() );
                // TODO: 21/06/2023 to call web service to get current weather
                isLoading=true;
                weatherRepository.getCurrentWeather(locationManager.getCityName(activity,
                                location.getLatitude(),location.getLongitude()),false)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess(currentWeatherResponse -> {
                            currentWeatherMutableLiveData.postValue(currentWeatherResponse);
                            Log.e("TAG", "customerLocation: "+ new Gson().toJson(currentWeatherResponse));
                        })
                        .doOnError(throwable -> {
                            Log.e("TAG", "customerLocation: "+throwable.getMessage() );
                            isLoading=false;
                        }).subscribe();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activity!=null){
            activity.finishAffinity();
        }

    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()== R.id.seeMore){
            fragment.postValue(new FMore());
        }
    }
    public CityWeatherAdapter getAdapter(Context context){
        if (adapter==null)
            adapter=new CityWeatherAdapter(context,this);
        return adapter;
    }
    public void onItemClicked(CurrentWeatherEntity entity) {
        cookies.setClickedEntity(entity);
        fragment.postValue(new FDetails());
    }

}
