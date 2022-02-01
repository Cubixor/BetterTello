package me.cubixor.bettertello;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class TelloSettings {

    private final SharedPreferences sharedPref;
    private final SharedPreferences.Editor editor;

    private int exposure;
    private int bitrate;
    private boolean quality;

    public TelloSettings(Activity activity) {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        exposure = sharedPref.getInt("exposure", 0);
        bitrate = sharedPref.getInt("bitrate", 0);
        quality = sharedPref.getBoolean("quality", false);
    }

    public int getExposure() {
        return exposure;
    }

    public void setExposure(int exposure) {
        this.exposure = exposure;
        editor.putInt("exposure", exposure);
        editor.apply();
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
        editor.putInt("bitrate", bitrate);
        editor.apply();
    }

    public boolean getQuality() {
        return quality;
    }

    public void setQuality(boolean quality) {
        this.quality = quality;
        editor.putBoolean("quality", quality);
        editor.apply();
    }
}
