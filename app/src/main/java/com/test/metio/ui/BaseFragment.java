package com.test.metio.ui;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BaseFragment extends Fragment {

    protected BaseViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (viewModel != null)
            viewModel.onPermissionsResult((BaseActivity) requireActivity(), requestCode, permissions, grantResults);
    }

    public void onBackPressed() {
        if (viewModel != null)
            viewModel.onBackPressed();
    }

}
