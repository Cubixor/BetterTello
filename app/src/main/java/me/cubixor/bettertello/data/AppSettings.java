package me.cubixor.bettertello.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.InputDevice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import me.cubixor.bettertello.App;
import me.cubixor.bettertello.controller.Controller;
import me.cubixor.bettertello.controller.ControllerUtils;
import me.cubixor.bettertello.tello.TelloAction;

public class AppSettings implements Serializable {

    private static AppSettings instance;
    private final SharedPreferences sharedPref;
    private final SharedPreferences.Editor editor;

    private List<Controller> controllers;


    public AppSettings() {
        instance = this;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(App.getInstance().getApplicationContext());
        editor = sharedPref.edit();

        loadControllers();
    }

    public static AppSettings getInstance() {
        return instance;
    }

    public int getExposure() {
        return sharedPref.getInt("exposure", 0);
    }

    public void setExposure(int exposure) {
        editor.putInt("exposure", exposure);
        editor.apply();
    }

    public int getBitrate() {
        return sharedPref.getInt("bitrate", 0);
    }

    public void setBitrate(int bitrate) {
        editor.putInt("bitrate", bitrate);
        editor.apply();
    }

    public boolean getQuality() {
        return sharedPref.getBoolean("quality", false);
    }

    public void setQuality(boolean quality) {
        editor.putBoolean("quality", quality);
        editor.apply();
    }

    public float getIFrameInterval() {
        return sharedPref.getFloat("iFrameInterval", 2);
    }

    public void setIFrameInterval(float iFrameInterval) {
        editor.putFloat("iFrameInterval", iFrameInterval);
        editor.apply();
    }

    public boolean isFastMode() {
        return sharedPref.getBoolean("fastMode", false);
    }

    public void setFastMode(boolean fastMode) {
        editor.putBoolean("fastMode", fastMode);
        editor.apply();
    }

    public boolean isPhotoMode() {
        return sharedPref.getBoolean("photoMode", true);
    }

    public void setPhotoMode(boolean photoMode) {
        editor.putBoolean("photoMode", photoMode);
        editor.apply();
    }

    public List<Controller> getControllers() {
        return controllers;
    }

    private void loadControllers() {
        Gson gson = new Gson();
        Type type = new TypeToken<LinkedList<Controller>>() {
        }.getType();
        String controllersString = sharedPref.getString("controllers", "");

        if (controllersString.equals("")) {
            this.controllers = new LinkedList<>();
        } else {
            this.controllers = new LinkedList<>(gson.fromJson(controllersString, type));
        }

        List<InputDevice> devices = ControllerUtils.getConnectedInputDevices();

        for (InputDevice inputDevice : devices) {
            Controller controller = ControllerUtils.getControllerByID(inputDevice.getDescriptor());
            if (controller == null) {
                addController(inputDevice);
            }
        }
    }

    private void saveControllers() {
        Gson gson = new Gson();
        String controllersString = gson.toJson(controllers);
        editor.putString("controllers", controllersString);
        editor.commit();
    }

    public void addController(InputDevice inputDevice) {
        Controller controller = new Controller(inputDevice.getDescriptor(), inputDevice.getName(), controllers.size() + 1);
        controllers.add(controller);

        saveControllers();
    }

    public void addControllerMapping(Controller controller, TelloAction telloAction, int keyCode) {
        controller.getMappings().put(keyCode, telloAction);

        saveControllers();
    }

    public void removeControllerMapping(Controller controller, TelloAction telloAction) {
        controller.getMappings().remove(controller.getKeyByAction(telloAction));

        saveControllers();
    }

    public void resetMappings(Controller controller) {
        controller.getMappings().clear();

        saveControllers();
    }
}
