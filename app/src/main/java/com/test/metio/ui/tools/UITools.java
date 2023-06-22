package com.test.metio.ui.tools;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class UITools {

    public static void setOnLoadingState(boolean is_on_loading_state, Activity activity, ConstraintLayout lyParent, int progressBar_id, int btn_txt_id) {

        try {
            for (int i = 0; i < lyParent.getChildCount(); i++) {
                View view = lyParent.getChildAt(i);
                if (view instanceof ProgressBar) {
                    //show progress
                    view.setVisibility(is_on_loading_state ? View.VISIBLE : View.GONE);
                } else if (view instanceof TextView) {
                    //opacity txt
                    view.setAlpha(is_on_loading_state ? 0.1f : 1);
                }
                //disable btn
                lyParent.setEnabled(!is_on_loading_state);
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

    }
}
