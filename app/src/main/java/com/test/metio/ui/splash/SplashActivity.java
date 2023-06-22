package com.test.metio.ui.splash;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.test.metio.R;
import com.test.metio.databinding.ActivitySplashBinding;
import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.main.MainActivityViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends BaseActivity {

    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        viewModel = new ViewModelProvider(this).get(SplashActivityViewModel.class);
        binding.setLifecycleOwner(this);
        ((SplashActivityViewModel) viewModel).initiateViewModel(this);
    }
}