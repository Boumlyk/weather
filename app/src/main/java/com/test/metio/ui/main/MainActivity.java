package com.test.metio.ui.main;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.test.metio.R;
import com.test.metio.databinding.ActivityMainBinding;
import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.main.homeFragment.FHomeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setLifecycleOwner(this);
        ((MainActivityViewModel) viewModel).initiateViewModel(this);
    }
}