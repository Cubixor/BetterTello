package me.cubixor.bettertello.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

import me.cubixor.bettertello.App;
import me.cubixor.bettertello.BuildConfig;

public class Utils {


    public static int dpToPixels(Context context, int dp) {
        return Math.round(dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getStr(int id) {
        return App.getInstance().getRes().getString(id);
    }

    public static String getStr(String aString) {
        int resId = App.getInstance().getRes().getIdentifier(aString, "string", BuildConfig.APPLICATION_ID);
        return getStr(resId);
    }

    public static void fullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // hide status bar and nav bar after a short delay, or if the user interacts with the middle of the screen
    }

}
