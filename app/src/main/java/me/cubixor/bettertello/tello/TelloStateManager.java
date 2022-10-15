package me.cubixor.bettertello.tello;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import me.cubixor.bettertello.Activities;
import me.cubixor.bettertello.HomePageActivity;
import me.cubixor.bettertello.MainActivity;
import me.cubixor.bettertello.R;
import me.cubixor.bettertello.bar.BarState;
import me.cubixor.bettertello.bar.BarStateManager;
import me.cubixor.telloapi.api.DroneStatus;
import me.cubixor.telloapi.api.listeners.DroneConnectionListener;
import me.cubixor.telloapi.api.listeners.DroneStatusListener;
import me.cubixor.telloapi.api.video.SmartVideoMode;

public class TelloStateManager implements DroneStatusListener, DroneConnectionListener {

    private final BarStateManager barStateManager;
    private final MainActivity view;
    private long lastPacketTime;
    private int lastPacketID = 0;
    private long flightTime = 0;


    public TelloStateManager() {
        barStateManager = new BarStateManager();
        view = MainActivity.getActivity();
    }

    @Override
    public void onConnect() {
        barStateManager.addState(BarState.READY_TO_FLY);
        barStateManager.removeState(BarState.NOT_CONNECTED);
        barStateManager.removeState(BarState.LOST_CONNECTION);
    }

    @Override
    public void onDisconnect() {
        HomePageActivity homePageActivity = Activities.getHomePage();
        if (homePageActivity != null) {
            barStateManager.shrinkStates(homePageActivity, homePageActivity.findViewById(R.id.expandStatesButton));
            barStateManager.hideExpandButton();
        }
        barStateManager.addState(BarState.LOST_CONNECTION);
    }

    @Override
    public void onLightStrengthPacketReceive(boolean lightOK) {
        barStateManager.addRemoveState(BarState.LOW_LIGHT, !lightOK);
    }

    @Override
    public void onWifiStrengthPacketReceive(int wifiStrength, int wifiInterference) {
        int resID;
        if (wifiStrength <= 20) {
            resID = R.drawable.ic_round_signal_wifi_0_bar_24;
        } else if (wifiStrength <= 40) {
            resID = R.drawable.ic_round_network_wifi_1_bar_24;
        } else if (wifiStrength <= 60) {
            resID = R.drawable.ic_round_network_wifi_2_bar_24;
        } else if (wifiStrength <= 80) {
            resID = R.drawable.ic_round_network_wifi_3_bar_24;
        } else {
            resID = R.drawable.ic_round_signal_wifi_4_bar_24;
        }

        barStateManager.addRemoveState(BarState.WEAK_WIFI_SIGNAL, wifiStrength <= 40);
        barStateManager.addRemoveState(BarState.STRONG_WIFI_INTERFERENCE, wifiInterference > 0);

        Drawable drawable = ResourcesCompat.getDrawable(view.getResources(), resID, null);

        view.runOnUiThread(() -> {
            ImageView imageView = view.findViewById(R.id.wifiIndicator);
            imageView.setImageDrawable(drawable);
        });

        HomePageActivity activity = Activities.getHomePage();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                ImageView imageView = activity.findViewById(R.id.wifiIndicator2);

                imageView.setImageDrawable(drawable);
            });
        }
    }

    @Override
    public void onStatusPacketReceive(DroneStatus droneStatus) {
        //System.out.println(droneStatus.toString() + "\n\n");

        //System.out.println("flightTime " + droneStatus.getFlyTime() + "   last packet " + (System.currentTimeMillis() - lastPacketTime));
        //System.out.println("left " + droneStatus.getDroneFlyTimeLeft());

        if (lastPacketID != droneStatus.getFlyTime()) {
            if (droneStatus.getFlyTime() == 0) {
                flightTime = 0;
            } else if (lastPacketID == droneStatus.getFlyTime() - 1) {
                flightTime += System.currentTimeMillis() - lastPacketTime;
            } else {
                flightTime += (droneStatus.getFlyTime() - lastPacketID) * 100L;
            }
        }

        lastPacketTime = System.currentTimeMillis();
        lastPacketID = droneStatus.getFlyTime();

        barStateManager.addRemoveState(BarState.LOW_BATTERY, droneStatus.isBatteryLow());
        barStateManager.addRemoveState(BarState.CRITICAL_BATTERY, droneStatus.isBatteryCritical());
        barStateManager.addRemoveState(BarState.BATTERY_ERROR, droneStatus.isBatteryError());
        barStateManager.addRemoveState(BarState.CAMERA_ERROR, droneStatus.isCameraError() != 0);
        barStateManager.addRemoveState(BarState.DOWNWARD_VISION_ERROR, droneStatus.isDownwardVisionError());
        barStateManager.addRemoveState(BarState.GRAVITY_ERROR, droneStatus.isGravityError());
        barStateManager.addRemoveState(BarState.IMU_ERROR, droneStatus.isImuError());
        barStateManager.addRemoveState(BarState.OVERHEAT, droneStatus.isOverheat());
        barStateManager.addRemoveState(BarState.TOO_WINDY, droneStatus.isTooWindy());

        HomePageActivity activity = Activities.getHomePage();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                TextView batteryText = activity.findViewById(R.id.batteryText2);
                batteryText.setText(activity.getString(R.string.battery_percentage, droneStatus.getBatteryPercentage()));
                ProgressBar batteryIndicator = activity.findViewById(R.id.batteryIndicator2);
                batteryIndicator.setProgress(droneStatus.getBatteryPercentage(), true);
            });
        }

        view.runOnUiThread(() -> {

            View takeoffLandButton = MainActivity.getActivity().findViewById(R.id.takeoffLandButton);

            if (!droneStatus.iseMSky()) {
                takeoffLandButton.setBackgroundResource(R.drawable.ic_round_flight_land_48);
            } else {
                takeoffLandButton.setBackgroundResource(R.drawable.ic_round_flight_takeoff_48);
            }


            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("mm:ss");
            Instant instant = Instant.ofEpochMilli(flightTime);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
            String time = dateTimeFormatter.format(localDateTime);
            TextView timeText = view.findViewById(R.id.timeText);
            timeText.setText(time);

            TextView batteryText = view.findViewById(R.id.batteryText);
            batteryText.setText(view.getString(R.string.battery_percentage, droneStatus.getBatteryPercentage()));
            ProgressBar batteryIndicator = view.findViewById(R.id.batteryIndicator);
            batteryIndicator.setProgress(droneStatus.getBatteryPercentage(), true);

            TextView speedText = view.findViewById(R.id.speedText);
            speedText.setText(view.getString(R.string.speed, view.getTello().getDroneState().getFlySpeed()));

            TextView heightText = view.findViewById(R.id.heightText);
            heightText.setText(view.getString(R.string.height, droneStatus.getHeight()));
        });


    }


    @Override
    public void onSmartVideoPacketReceive(SmartVideoMode smartVideoMode, boolean running) {
        MainActivity.getActivity().setFlightModeRunning(running);
    }
}
