package com.test.metio.ui.main;

import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.BaseViewModel;
import com.test.metio.ui.main.homeFragment.FHome;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainActivityViewModel extends BaseViewModel {


    @Inject
    public MainActivityViewModel() {
    }

    @Override
    public void initiateViewModel(BaseActivity activity) {
        super.initiateViewModel(activity);
        fragment.postValue(new FHome());
    }
}
