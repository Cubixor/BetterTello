package me.cubixor.bettertello.bar

import me.cubixor.bettertello.R

enum class BarState(val textPath: Int, val barColor: BarColor, val priority: Int) {
    READY_TO_FLY(R.string.state_ready_to_fly, BarColor.OK, 0),
    FLYING(R.string.state_flying, BarColor.OK, 0),
    NOT_CONNECTED(R.string.state_not_connected, BarColor.NEUTRAL, 0),
    LOST_CONNECTION(R.string.state_lost_connection, BarColor.ERROR, 4),
    LOW_BATTERY(R.string.state_low_battery, BarColor.WARNING, 2),
    CRITICAL_BATTERY(R.string.state_critical_battery, BarColor.ERROR, 4),
    BATTERY_ERROR(R.string.state_battery_error, BarColor.ERROR, 4),
    CAMERA_ERROR(R.string.state_camera_error, BarColor.ERROR, 4),
    DOWNWARD_VISION_ERROR(R.string.state_downward_vision_error, BarColor.ERROR, 4),
    GRAVITY_ERROR(R.string.state_gravity_error, BarColor.ERROR, 4),
    IMU_ERROR(R.string.state_imu_error, BarColor.ERROR, 4),
    OVERHEAT(R.string.state_overheat, BarColor.ERROR, 4),
    TOO_WINDY(R.string.state_too_windy, BarColor.WARNING, 3),
    WEAK_WIFI_SIGNAL(R.string.state_weak_wifi_signal, BarColor.WARNING, 2),
    STRONG_WIFI_INTERFERENCE(R.string.state_strong_wifi_interference, BarColor.WARNING, 2),
    LOW_LIGHT(R.string.state_low_light, BarColor.WARNING, 1);

}