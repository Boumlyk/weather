package com.test.metio.tools.security;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.scottyab.rootbeer.RootBeer;

import java.security.MessageDigest;

public class AuthenticityChecker {

    public static boolean checkAuthenticity(Context context){
      RootBeer rootBeer = new RootBeer(context);
      return/* (!rootBeer.isRooted())*/true;
    }

    public static String getCurrentSignature(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                return HashManager.toHex(MessageDigest.getInstance("SHA-256").digest(signature.toByteArray())).toLowerCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
