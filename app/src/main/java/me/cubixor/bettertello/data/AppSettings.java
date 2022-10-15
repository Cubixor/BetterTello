package me.cubixor.bettertello.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.InputDevice;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import me.cubixor.bettertello.Activities;
import me.cubixor.bettertello.MainActivity;
import me.cubixor.bettertello.R;
import me.cubixor.bettertello.controller.Controller;
import me.cubixor.bettertello.controller.ControllerUtils;
import me.cubixor.bettertello.tello.TelloAction;
import me.cubixor.bettertello.tello.VideoSettingsWindow;
import me.cubixor.bettertello.utils.VideoUtils;
import me.cubixor.telloapi.api.Tello;
import me.cubixor.telloapi.api.video.BitRate;

public class AppSettings implements Serializable {

    private static AppSettings instance;
    private final Tello tello;

    private final SharedPreferences sharedPref;
    private final SharedPreferences.Editor editor;

    private List<Controller> controllers;

    private int exposure;
    private int bitrate;
    private boolean quality;
    private float iFrameInterval;

    public AppSettings(Activity activity, Tello tello) {
        instance = this;
        this.tello = tello;

        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        exposure = sharedPref.getInt("exposure", 0);
        bitrate = sharedPref.getInt("bitrate", 0);
        quality = sharedPref.getBoolean("quality", false);
        iFrameInterval = sharedPref.getFloat("iFrameInterval", 2);

        loadControllers();
    }

    public static AppSettings getInstance() {
        return instance;
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

    public int getExposure() {
        return exposure;
    }

    public void setExposure(int exposure) {
        this.exposure = exposure;
        editor.putInt("exposure", exposure);
        editor.apply();

        tello.getVideoInfo().setExposure(exposure - 9);

        MainActivity.getActivity().updateExposureValue(exposure);

        VideoSettingsWindow videoSettingsWindow = Activities.getVideoSettingsWindow();
        if (videoSettingsWindow == null) {
            return;
        }

        TextView videoSettingsTextView = videoSettingsWindow.getPopupView().findViewById(R.id.exposureValue);
        videoSettingsTextView.setText(VideoUtils.getExposureString(exposure));
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
        editor.putInt("bitrate", bitrate);
        editor.apply();

        tello.getVideoInfo().setBitRate(BitRate.values()[bitrate]);

        MainActivity.getActivity().updateBitrateValue(bitrate);

        VideoSettingsWindow videoSettingsWindow = Activities.getVideoSettingsWindow();
        if (videoSettingsWindow == null) {
            return;
        }

        TextView videoSettingsTextView = videoSettingsWindow.getPopupView().findViewById(R.id.bitrateValue);
        videoSettingsTextView.setText(VideoUtils.getBitrateString(bitrate));
    }

    public boolean getQuality() {
        return quality;
    }

    public void setQuality(boolean quality) {
        this.quality = quality;
        editor.putBoolean("quality", quality);
        editor.apply();

        tello.getVideoInfo().setJPEGQuality(quality);

        VideoSettingsWindow videoSettingsWindow = Activities.getVideoSettingsWindow();
        if (videoSettingsWindow == null) {
            return;
        }

        TextView qualityValue = videoSettingsWindow.getPopupView().findViewById(R.id.qualityValue);
        qualityValue.setText(VideoUtils.getQualityString(quality));
    }

    public float getIFrameInterval() {
        return iFrameInterval;
    }

    public void setIFrameInterval(float iFrameInterval) {
        this.iFrameInterval = iFrameInterval;
        editor.putFloat("iFrameInterval", iFrameInterval);
        editor.apply();

        tello.getVideoInfo().setIFrameInterval((int) (iFrameInterval * 1000));
    }

    public List<Controller> getControllers() {
        return controllers;
    }

    public void addController(InputDevice inputDevice) {
        Controller controller = new Controller(inputDevice.getDescriptor(), inputDevice.getName());
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
