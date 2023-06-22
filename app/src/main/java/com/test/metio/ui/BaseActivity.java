package com.test.metio.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.test.metio.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BaseActivity extends AppCompatActivity {

    public static final String ACTION_START_LOADING = "START_LOADING";
    public static final String ACTION_FINISH_LOADING = "FINISH_LOADING";

    public static final String ACTION_START_LOADING_DIALOG = "START_LOADING_DIALOG";
    public static final String ACTION_FINISH_LOADING_DIALOG = "FINISH_LOADING_DIALOG";


    protected BaseViewModel viewModel;

    @Override
    public void onBackPressed() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment != null)
            fragment.onBackPressed();
        if (viewModel != null)
            viewModel.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewModel!=null)
            viewModel.storeCookies();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment != null)
            fragment.onPermissionsResult(requestCode, permissions, grantResults);
        if (viewModel != null)
            viewModel.onPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
