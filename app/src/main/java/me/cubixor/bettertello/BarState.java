package me.cubixor.bettertello;

public enum BarState {
    READY_TO_FLY(R.string.state_ready_to_fly, BarColor.OK,0),
    FLYING(R.string.state_flying, BarColor.OK,0),
    NOT_CONNECTED(R.string.state_not_connected, BarColor.NEUTRAL,0),
    LOST_CONNECTION(R.string.state_lost_connection, BarColor.ERROR,3),
    LOW_BATTERY(R.string.state_low_battery, BarColor.WARNING,2),
    CRITICAL_BATTERY(R.string.state_critical_battery, BarColor.ERROR,3),
    WEAK_WIFI_SIGNAL(R.string.state_weak_wifi_signal, BarColor.WARNING,2),
    STRONG_WIFI_INTERFERENCE(R.string.state_strong_wifi_interference, BarColor.WARNING,2),
    LOW_LIGHT(R.string.state_low_light, BarColor.WARNING,1);

    private final int textPath;
    private final BarColor barColor;
    private final int priority;

    BarState(int textPath, BarColor color,int priority) {
        this.textPath = textPath;
        this.barColor = color;
        this.priority = priority;
    }

    public int getTextPath() {
        return textPath;
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public int getPriority() {
        return priority;
    }
}
