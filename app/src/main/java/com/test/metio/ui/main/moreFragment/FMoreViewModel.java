package com.test.metio.ui.main.moreFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.test.metio.R;
import com.test.metio.data.WeatherRepository;
import com.test.metio.module.weather.CurrentWeatherEntity;
import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.BaseViewModel;
import com.test.metio.ui.adapter.CityWeatherAdapter;
import com.test.metio.ui.main.detailFragment.FDetails;
import com.test.metio.ui.main.homeFragment.FHome;

import java.util.Collections;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class FMoreViewModel extends BaseViewModel {

    @Inject
    WeatherRepository weatherRepository;
    public MutableLiveData<String> addedCity = new MutableLiveData<>("");
    CityWeatherAdapter adapter;

    @Inject
    public FMoreViewModel() {
    }

    @Override
    public void initiateViewModel(BaseActivity activity) {
        super.initiateViewModel(activity);
        getData();
    }

    private void getData() {
        weatherRepository.getListOfWeather().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(currentWeatherResponse -> {
                    if (!currentWeatherResponse.isEmpty())
                        adapter.feedList(currentWeatherResponse);
                })
                .doOnError(throwable -> {
                    if (throwable != null)
                        Log.e("TAG", "initiateViewModel: " + throwable.getMessage());
                    else
                        Log.e("TAG", "initiateViewModel:error null ");

                }).subscribe();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fragment.postValue(new FHome());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.oui:
                addCityToList();
                break;
            case R.id.non:
                actions.postValue(Collections.singletonList(BaseActivity.ACTION_FINISH_LOADING_DIALOG));
                break;
            case R.id.floatingButton:
                actions.postValue(Collections.singletonList(BaseActivity.ACTION_START_LOADING_DIALOG));
                break;
        }
    }

    private void addCityToList() {
        if (addedCity.getValue() != null && !addedCity.getValue().isEmpty()) {
            actions.postValue(Collections.singletonList(BaseActivity.ACTION_START_LOADING));
            weatherRepository.getCurrentWeather(addedCity.getValue(),true)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(currentWeatherResponse -> {
                        if (currentWeatherResponse != null && currentWeatherResponse.getName() != null) {
                            addedCity.postValue("");
                            cookies.getListOfCity().add(currentWeatherResponse.getName());
                            adapter.addItemToList(currentWeatherResponse);
                            Log.e("TAG", "addCityToList: " + new Gson().toJson(currentWeatherResponse));
                        } else {
                            message.postValue("City not found");
                        }
                    })
                    .doOnError(throwable -> {
                        Log.e("TAG", "addCityToList:error ");
                        message.postValue("City not found");
                    }).doFinally(() -> actions.postValue(Collections.singletonList(BaseActivity.ACTION_FINISH_LOADING_DIALOG))
                    ).subscribe();
        } else {
            message.postValue("Enter city name !");
        }
    }

    public void onSearchTextChanged(CharSequence s, int start, int before, int count) {
        adapter.getFilter().filter(s);
    }

    public AppCompatEditText.OnFocusChangeListener getOnEmailFocusChanged() {
        return (v, focus) -> {
//            hasFocus.postValue(focus);
        };
    }

    public CityWeatherAdapter getAdapter(Context context) {
        if (adapter == null)
            adapter = new CityWeatherAdapter(context, this);
        return adapter;
    }

    public void onItemClicked(CurrentWeatherEntity entity) {
        cookies.setClickedEntity(entity);
        fragment.postValue(new FDetails());
    }


}
