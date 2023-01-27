package me.cubixor.bettertello.utils;

import static me.cubixor.bettertello.utils.Utils.getStr;

import me.cubixor.bettertello.R;
import me.cubixor.telloapi.api.video.BitRate;
import me.cubixor.telloapi.api.video.VideoInfo;

public class VideoUtils {

    /**
     * Change bitrate from an int used in a progress bar to a String used in a textview
     *
     * @param progress Bitrate in integer format
     * @return Bitrate in formatted to string
     */
    public static int getBitrateString(int progress) {
        switch (BitRate.values()[progress]) {
            case MBPS_1:
                return R.string.bitrate_1;
            case MBPS_1_5:
                return R.string.bitrate_1_5;
            case MBPS_2:
                return R.string.bitrate_2;
            case MBPS_3:
                return R.string.bitrate_3;
            case MBPS_4:
                return R.string.bitrate_4;
            default:
                return R.string.bitrate_auto;
        }
    }

    /**
     * Change exposure from an int used in a progress bar to a String used in a textview
     *
     * @param progress Exposure in integer format
     * @return Exposure in formatted to string
     */
    public static String getExposureString(int progress) {
        float exposure = VideoInfo.getExposureValues()[progress];
        return exposure == 0.0f ? getStr(R.string.exposure_auto) : Float.toString(exposure);
    }
}
