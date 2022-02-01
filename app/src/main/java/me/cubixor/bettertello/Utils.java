package me.cubixor.bettertello;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {


    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String getStr(int id){
        return MainActivity.getInstance().getString(id);
    }

    public static String getStr(String aString) {
        String packageName = MainActivity.getInstance().getPackageName();
        int resId = MainActivity.getInstance().getResources().getIdentifier(aString, "string", packageName);
        return getStr(resId);
    }
}
