package com.test.metio.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.test.metio.R;
import com.test.metio.data.CookiesRepository;
import com.test.metio.data.WeatherRepository;
import com.test.metio.module.cookies.Cookies;
import com.test.metio.module.weather.CurrentWeatherEntity;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class BaseViewModel extends ViewModel {

    @Inject
    public Cookies cookies;

   /* @Inject
    public PermissionManager permissionManager;*/
    @Inject
    WeatherRepository weatherRepository;


    public MutableLiveData<List<String>> actions;

    public MutableLiveData<String> message;
    public MutableLiveData<Fragment> fragment;
    public MutableLiveData<Pair<Intent, Class<?>>> intentClass;


    @Inject
    CookiesRepository cookiesRepository;
    public MutableLiveData<String> title;


    @Inject
    public BaseViewModel() {
    }

    public void initiateViewModel(BaseActivity activity) {

        actions = new MutableLiveData<>();
        message = new MutableLiveData<>();
        fragment = new MutableLiveData<>();
        intentClass = new MutableLiveData<>();

        title = new MutableLiveData<>();

        message.observe(activity, msg -> displayMessage(activity,msg));

        intentClass.observe(activity, value -> {
            activity.startActivity(value.first.setClass(activity, value.second));
        });

        fragment.observe(activity, fragment -> {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment, fragment.getTag())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void displayMessage(BaseActivity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }


    public void onNextPressed() {
    }

    public void onClick(View v) {
    }

    public void onBackPressed() {
    }


    public void onPermissionsResult(BaseActivity requireActivity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    public void onItemDeleted(CurrentWeatherEntity entity) {
        weatherRepository.deleteItem(entity).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(aBoolean -> {
                    if (aBoolean){
                        cookies.getListOfCity().remove(entity.getName());
                        message.postValue("Success");
                    }else{
                        message.postValue("Error");
                    }
                }).subscribe();
    }

    public void storeCookies(){
        cookiesRepository.updateCookies(cookies).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


}
