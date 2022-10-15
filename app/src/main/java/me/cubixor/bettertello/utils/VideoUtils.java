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
    public static String getBitrateString(int progress) {
        BitRate bitrate = BitRate.values()[progress];
        String text;
        switch (bitrate) {
            case MBPS_1:
                text = Utils.getStr(R.string.bitrate_1);
                break;
            case MBPS_1_5:
                text = getStr(R.string.bitrate_1_5);
                break;
            case MBPS_2:
                text = getStr(R.string.bitrate_2);
                break;
            case MBPS_3:
                text = getStr(R.string.bitrate_3);
                break;
            case MBPS_4:
                text = getStr(R.string.bitrate_4);
                break;
            default:
                text = getStr(R.string.bitrate_auto);
                break;
        }
        return text;
    }

    public static String getQualityString(boolean quality) {
        return getStr(quality ? R.string.pic_quality_high : R.string.pic_quality_low);
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
