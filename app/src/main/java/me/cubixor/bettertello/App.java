package me.cubixor.bettertello;

import android.app.Application;
import android.content.res.Resources;

import me.cubixor.bettertello.bar.BarStateManager;
import me.cubixor.bettertello.controller.ControllerManager;
import me.cubixor.bettertello.data.AppSettings;
import me.cubixor.bettertello.data.AppSettingsRepository;
import me.cubixor.bettertello.data.FileManager;
import me.cubixor.bettertello.tello.TelloStateManager;
import me.cubixor.telloapi.api.Tello;

//TODO Change class instances to dependency injection
public class App extends Application {

    private static App mInstance;
    private BarStateManager barStateManager;
    private TelloStateManager telloStateManager;
    private ControllerManager controllerManager;

    private AppSettingsRepository appSettingsRepository;
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

    public TelloStateManager getTelloStateManager() {
        return telloStateManager;
    }

    public ControllerManager getControllerManager() {
        return controllerManager;
    }

    public AppSettingsRepository getAppSettingsRepository() {
        return appSettingsRepository;
    }

    public Tello getTello() {
        return tello;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        tello = Tello.build();
        appSettingsRepository = new AppSettingsRepository(new AppSettings());
        barStateManager = new BarStateManager();
        telloStateManager = new TelloStateManager();
        controllerManager = new ControllerManager();

        telloSetup();
    }

    private void telloSetup() {
        TelloStateManager telloStateManager = new TelloStateManager();
        tello.addConnectionListener(telloStateManager);
        tello.addDroneStatusListener(telloStateManager);
        tello.addFileListener(new FileManager());
    }

}
