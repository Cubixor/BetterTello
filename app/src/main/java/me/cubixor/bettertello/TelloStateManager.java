package me.cubixor.bettertello;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.cubixor.telloapi.api.DroneConnectionListener;
import me.cubixor.telloapi.api.DroneStatus;
import me.cubixor.telloapi.api.DroneStatusListener;

public class TelloStateManager implements DroneStatusListener, DroneConnectionListener {

    BarUtils barUtils;

    public TelloStateManager() {
        barUtils = new BarUtils();


/*
        ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
        s.schedule(() -> {
        }, 2, TimeUnit.SECONDS);
*/
    }

    @Override
    public void onConnect() {
        barUtils.addState(BarState.READY_TO_FLY);
    }

    @Override
    public void onDisconnect() {
        barUtils.getActiveStates().clear();
        barUtils.addState(BarState.LOST_CONNECTION);
    }

    @Override
    public void onLightStrengthPacketReceive(boolean lightOK) {
        if (barUtils.getActiveStates().contains(BarState.LOW_LIGHT)) {
            if (lightOK) {
                barUtils.removeState(BarState.LOW_LIGHT);
            }
        } else if (!lightOK) {
            barUtils.addState(BarState.LOW_LIGHT);
        }
    }

    @Override
    public void onWifiStrengthPacketReceive(int wifiStrength, int wifiInterference) {
    }

    @Override
    public void onStatusPacketReceive(DroneStatus droneStatus) {
        System.out.println(droneStatus.toString() + "\n\n");
    }
}
