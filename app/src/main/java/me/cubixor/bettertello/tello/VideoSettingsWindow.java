package me.cubixor.bettertello.tello;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import me.cubixor.bettertello.Activities;
import me.cubixor.bettertello.R;
import me.cubixor.bettertello.data.AppSettings;
import me.cubixor.bettertello.utils.Utils;
import me.cubixor.bettertello.utils.VideoUtils;
import me.cubixor.telloapi.api.Tello;

public class VideoSettingsWindow {

    private final Activity activity;
    private final AppSettings appSettings;
    private View popupView;
    private boolean open;

    public VideoSettingsWindow(Activity activity, Tello tello) {
        this.activity = activity;
        appSettings = AppSettings.getInstance();
    }

    public void create() {
        open = true;
        Activities.updateVideoSettingsWindow(this);

        //Setup popup window
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.video_settings, null);

        int width = Utils.dpToPixels(activity, 600);
        int height = Utils.dpToPixels(activity, 200);
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
                .setBlurAutoUpdate(true);


        //Setup exposure text, bar and current value text.
        SeekBar exposureBar = popupView.findViewById(R.id.exposureBar);
        TextView exposureValue = popupView.findViewById(R.id.exposureValue);

        exposureValue.setText(VideoUtils.getExposureString(appSettings.getExposure()));
        exposureBar.setProgress(appSettings.getExposure());

        exposureBar.setOnSeekBarChangeListener(onExposureChange());


        //Setup bitrate text, bar and current value text
        SeekBar bitrateBar = popupView.findViewById(R.id.bitrateBar);
        TextView bitrateValue = popupView.findViewById(R.id.bitrateValue);

        bitrateValue.setText(VideoUtils.getBitrateString(appSettings.getBitrate()));
        bitrateBar.setProgress(appSettings.getBitrate());

        bitrateBar.setOnSeekBarChangeListener(onBitrateChange());

        //Setup picture quality text, switch and current value text
        SwitchCompat qualitySwitch = popupView.findViewById(R.id.qualitySwitch);
        TextView qualityValue = popupView.findViewById(R.id.qualityValue);

        qualityValue.setText(VideoUtils.getQualityString(appSettings.getQuality()));
        qualitySwitch.setChecked(appSettings.getQuality());

        qualitySwitch.setOnCheckedChangeListener(onQualityChangeListener());

        EditText editText = popupView.findViewById(R.id.iframeTextBox);
        editText.setText(Float.toString(appSettings.getIFrameInterval()));


        popupWindow.setAnimationStyle(R.style.window_animation);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

        Thread blurThread = new Thread(() -> {
            while (true) {
                blurView.invalidate();
            }
        });
        blurThread.start();

        popupWindow.setOnDismissListener(() -> {
            try {
                float f = Float.parseFloat(editText.getText().toString());
                appSettings.setIFrameInterval(f);

                blurThread.interrupt();
                open = false;
            } catch (NumberFormatException ignored) {
            }
        });
    }

    public SeekBar.OnSeekBarChangeListener onExposureChange() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                appSettings.setExposure(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
    }


    public SeekBar.OnSeekBarChangeListener onBitrateChange() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                appSettings.setBitrate(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
    }


    public CompoundButton.OnCheckedChangeListener onQualityChangeListener() {
        return (buttonView, isChecked) -> appSettings.setQuality(isChecked);
    }

    public boolean isOpen() {
        return open;
    }

    public View getPopupView() {
        return popupView;
    }
}
