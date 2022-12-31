package me.cubixor.bettertello;

import android.app.Application;
import android.content.res.Resources;

import me.cubixor.bettertello.bar.BarStateManager;
import me.cubixor.bettertello.data.FileManager;
import me.cubixor.bettertello.tello.TelloStateManager;
import me.cubixor.telloapi.api.Tello;

public class App extends Application {

    private static App mInstance;
    private BarStateManager barStateManager;
    private Tello tello;

    public static App getInstance() {
        return mInstance;
    }

    public Resources getRes() {
        return getResources();
    }

    public BarStateManager getBarStateManager() {
        return barStateManager;
    }

    public Tello getTello() {
        return tello;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        barStateManager = new BarStateManager();

        telloSetup();
    }

    private void telloSetup() {
        tello = Tello.build();

        TelloStateManager telloStateManager = new TelloStateManager();
        tello.addConnectionListener(telloStateManager);
        tello.addDroneStatusListener(telloStateManager);
        tello.addFileListener(new FileManager());
    }

}
