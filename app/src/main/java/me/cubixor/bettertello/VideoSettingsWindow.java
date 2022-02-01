package me.cubixor.bettertello;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import static me.cubixor.bettertello.Utils.convertDpToPixel;
import static me.cubixor.bettertello.Utils.getStr;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import me.cubixor.telloapi.api.Tello;
import me.cubixor.telloapi.api.VideoInfo;

public class VideoSettingsWindow {

    private final Activity activity;
    private final Tello tello;
    private final TelloSettings telloSettings;

    public VideoSettingsWindow(Activity activity, Tello tello, TelloSettings telloSettings) {
        this.activity = activity;
        this.tello = tello;
        this.telloSettings = telloSettings;
    }

    public void create() {
        //Setup popup window
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.video_settings, null);

        int width = (int) convertDpToPixel(600, activity);
        int height = (int) convertDpToPixel(200, activity);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);


        //Setup blur
        View decorView = activity.getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();


        BlurView blurView = popupView.findViewById(R.id.videoSettingsBlur);
        blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        blurView.setClipToOutline(true);
        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(activity))
                .setBlurRadius(1f)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(false);


        //Setup exposure text, bar and current value text.
        SeekBar exposureBar = popupView.findViewById(R.id.exposureBar);
        TextView exposureValue = popupView.findViewById(R.id.exposureValue);

        exposureValue.setText(getExposureString(telloSettings.getExposure()));
        exposureBar.setProgress(telloSettings.getExposure());

        exposureBar.setOnSeekBarChangeListener(onExposureChange(exposureValue));


        //Setup bitrate text, bar and current value text
        SeekBar bitrateBar = popupView.findViewById(R.id.bitrateBar);
        TextView bitrateValue = popupView.findViewById(R.id.bitrateValue);

        bitrateValue.setText(getBitrateString(telloSettings.getBitrate()));
        bitrateBar.setProgress(telloSettings.getBitrate());

        bitrateBar.setOnSeekBarChangeListener(onBitrateChange(bitrateValue));

        //Setup picture quality text, switch and current value text
        SwitchCompat qualitySwitch = popupView.findViewById(R.id.qualitySwitch);
        TextView qualityValue = popupView.findViewById(R.id.qualityValue);

        qualityValue.setText(getQualityString(telloSettings.getQuality()));
        qualitySwitch.setChecked(telloSettings.getQuality());

        qualitySwitch.setOnCheckedChangeListener(onQualityChangeListener(qualityValue));


        //Show window
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        new Thread(){
            @Override
            public void run(){
                while(true){
                    blurView.invalidate();
                }
            }
        }.start();
    }

    public SeekBar.OnSeekBarChangeListener onExposureChange(TextView exposureValue) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                exposureValue.setText(getExposureString(progress));
                telloSettings.setExposure(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tello.getPacketSender().sendSetExposurePacket(seekBar.getProgress() - 9);
            }
        };
    }

    /**
     * Change exposure from an int used in a progress bar to a String used in a textview
     *
     * @param progress Exposure in integer format
     * @return Exposure in formatted to string
     */
    private String getExposureString(int progress) {
        float exposure = tello.getPacketSender().getExposureValues()[progress];
        return exposure == 0.0f ? getStr(R.string.exposure_auto) : Float.toString(exposure);
    }

    public SeekBar.OnSeekBarChangeListener onBitrateChange(TextView bitrateValue) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bitrateValue.setText(getBitrateString(progress));
                telloSettings.setBitrate(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tello.getPacketSender().sendSetBitratePacket(VideoInfo.BitRate.values()[seekBar.getProgress()]);
            }
        };
    }

    /**
     * Change bitrate from an int used in a progress bar to a String used in a textview
     *
     * @param progress Bitrate in integer format
     * @return Bitrate in formatted to string
     */
    private String getBitrateString(int progress) {
        VideoInfo.BitRate bitrate = VideoInfo.BitRate.values()[progress];
        String text;
        switch (bitrate) {
            case MBPS_1:
                text = getStr(R.string.bitrate_1);
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

    public CompoundButton.OnCheckedChangeListener onQualityChangeListener(TextView qualityValue) {
        return (buttonView, isChecked) -> {
            qualityValue.setText(getQualityString(isChecked));
            telloSettings.setQuality(isChecked);
            tello.getPacketSender().sendJPEGQualityPacket(isChecked);
        };
    }

    private String getQualityString(boolean quality) {
        return getStr(quality ? R.string.pic_quality_high : R.string.pic_quality_low);
    }
}
