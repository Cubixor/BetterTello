package me.cubixor.bettertello;

import android.app.Application;
import android.content.res.Resources;

import me.cubixor.bettertello.bar.BarStateManager;

public class App extends Application {

    //private static App mInstance;
    private static Resources res;
    private static BarStateManager barStateManager;

    public static Resources getRes() {
        return res;
    }

    /*public static App getInstance() {
        return mInstance;
    }*/

    public static BarStateManager getBarStateManager() {
        return barStateManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //mInstance = this;
        res = getResources();
        barStateManager = new BarStateManager();
    }

}
