package com.test.metio.ui.splash;

import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.test.metio.tools.permissions.PermissionManager;
import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.BaseViewModel;
import com.test.metio.ui.main.MainActivity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SplashActivityViewModel extends BaseViewModel {


    @Inject
    PermissionManager permissionManager;

    @Inject
    public SplashActivityViewModel() {
    }

    @Override
    public void initiateViewModel(BaseActivity activity) {
        super.initiateViewModel(activity);
        if (permissionManager.checkPermissions(PermissionManager.LOCATION_PERMISSIONS))
            start();
        else
            permissionManager.requestLocationPermissions(activity);
    }

    private void start() {
        intentClass.postValue(new Pair<>(new Intent(), MainActivity.class));
    }

    @Override
    public void onPermissionsResult(BaseActivity requireActivity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionManager.REQUEST_CODE == requestCode)
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            } else {
                message.postValue("Permission required");
            }
    }
}
