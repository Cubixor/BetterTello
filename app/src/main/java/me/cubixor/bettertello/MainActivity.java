package me.cubixor.bettertello;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import org.bytedeco.javacv.AndroidFrameConverter;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import me.cubixor.telloapi.api.DroneConnectionListener;
import me.cubixor.telloapi.api.Tello;
import me.cubixor.telloapi.api.VideoInfo;

public class MainActivity extends AppCompatActivity {

    Tello tello;
    TelloInfo telloInfo = new TelloInfo();
    TelloSettings telloSettings;
    TelloStateManager telloStateManager;

    public static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        //changeBarColor(BarState.OK, BarState.ERROR);

        telloSettings = new TelloSettings(this);
        telloStateManager = new TelloStateManager();

        float radius = 1f;

        View decorView = getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        BlurView blurView = findViewById(R.id.blurView);
        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true); // Or false if it's in a scrolling container or might be animated


        AndroidFrameConverter conv = new AndroidFrameConverter();
        ImageView imageFrame = findViewById(R.id.video);

        tello = Tello.build();
        tello.addConnectionListener(telloStateManager);
        tello.addDroneStatusListener(telloStateManager);

        tello.addVideoListener((frame) -> {
            Bitmap bitmap = conv.convert(frame);
            imageFrame.setImageBitmap(bitmap);
        });

        JoystickView joystickLeft = findViewById(R.id.joystickLeft);
        joystickLeft.setOnMoveListener((angle, strength) -> {
            tello.setYaw(joystickLeft.getNormalizedX());
            tello.setThrottle(joystickLeft.getNormalizedY());
        });
        JoystickView joystickRight = findViewById(R.id.joystickRight);
        joystickRight.setOnMoveListener((angle, strength) -> {
            tello.setRoll(joystickRight.getNormalizedX());
            tello.setPitch(joystickRight.getNormalizedY());
        });


        SwitchCompat onOffSwitch = findViewById(R.id.slowFastSwitch);
        onOffSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tello.setFastMode(isChecked);
        });
    }


    public void onTakeoffLandButtonClick(View view) {
        if (!telloInfo.isFlying()) {
            tello.getPacketSender().sendTakeOffPacket();
            view.setBackgroundResource(R.drawable.land);
            telloInfo.setFlying(true);
        } else {
            tello.getPacketSender().sendLandPacket();
            view.setBackgroundResource(R.drawable.takeoff);
            telloInfo.setFlying(false);
        }
    }

    public void onChangePhotoVideoButtonClick(View view) {
        if (tello.getVideoInfo().getVideoMode().equals(VideoInfo.VideoMode.PHOTO)) {
            tello.getPacketSender().sendChangeVideoAspectPacket(VideoInfo.VideoMode.VIDEO);
            findViewById(R.id.takePicRecordButton).setBackgroundResource(R.drawable.video_btn);
        } else {
            tello.getPacketSender().sendChangeVideoAspectPacket(VideoInfo.VideoMode.PHOTO);
            findViewById(R.id.takePicRecordButton).setBackgroundResource(R.drawable.photo_btn);
        }
    }

    public void onVideoSettingsButtonClick(View view) {
        new VideoSettingsWindow(this, tello, telloSettings).create();
    }
}