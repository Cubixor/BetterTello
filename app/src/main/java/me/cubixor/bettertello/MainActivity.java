package me.cubixor.bettertello;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import me.cubixor.bettertello.api.JoystickView;
import me.cubixor.bettertello.api.OnSwipeListener;
import me.cubixor.bettertello.controller.ControllerManager;
import me.cubixor.bettertello.controller.ControllerUtils;
import me.cubixor.bettertello.data.AppSettings;
import me.cubixor.bettertello.data.FileManager;
import me.cubixor.bettertello.databinding.ActivityMainBinding;
import me.cubixor.bettertello.tello.TelloAction;
import me.cubixor.bettertello.tello.TelloStateManager;
import me.cubixor.bettertello.tello.VideoSettingsWindow;
import me.cubixor.bettertello.utils.Utils;
import me.cubixor.bettertello.utils.VideoUtils;
import me.cubixor.telloapi.api.FlipDirection;
import me.cubixor.telloapi.api.Tello;
import me.cubixor.telloapi.api.listeners.DroneConnectionListener;

public class MainActivity extends AppCompatActivity implements DroneConnectionListener {

    private static MainActivity instance;
    private AppSettings appSettings;
    private Tello tello;
    private ControllerManager controllerManager;
    private boolean flightModeRunning = false;

    public static MainActivity getActivity() {
        return instance;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.slide_in_main, R.anim.slide_out_home);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.fullScreen(this);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        View decorView = getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        BlurView blurView = findViewById(R.id.blurView);
        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(0.1f)
                .setBlurAutoUpdate(true);


        tello = Tello.build();
        tello.addConnectionListener(this);
        TelloStateManager telloStateManager = new TelloStateManager();
        tello.addConnectionListener(telloStateManager);
        tello.addDroneStatusListener(telloStateManager);
        tello.addFileListener(new FileManager());

        VideoView videoView = new VideoView();
        tello.addVideoListener(videoView);

        appSettings = new AppSettings(this, tello);
        updateExposureValue(appSettings.getExposure());
        updateBitrateValue(appSettings.getBitrate());

        controllerManager = new ControllerManager();

        JoystickView joystickLeft = findViewById(R.id.joystickLeft);
        joystickLeft.setOnMoveListener((angle, strength) -> {
            tello.getDroneAxis().setYaw(joystickLeft.getNormalizedX());
            tello.getDroneAxis().setThrottle(joystickLeft.getNormalizedY());
        });


        JoystickView joystickRight = findViewById(R.id.joystickRight);
        joystickRight.setOnMoveListener((angle, strength) -> {
            tello.getDroneAxis().setRoll(joystickRight.getNormalizedX());
            tello.getDroneAxis().setPitch(joystickRight.getNormalizedY());
        });

        GestureDetectorCompat detector = new GestureDetectorCompat(this, new OnSwipeListener() {
            @Override
            public boolean onSwipe(FlipDirection direction) {
                System.out.println(direction);

                TelloAction.handleFlip(tello, direction);

                return true;
            }
        });

        View flipsView = findViewById(R.id.flipsView);
        View touchIndicatorView = findViewById(R.id.flipsTouchIndicatorView);
        flipsView.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    updateFlipsTouchIndicatorPosition(touchIndicatorView, event);
                    touchIndicatorView.setVisibility(View.VISIBLE);
                    break;
                case MotionEvent.ACTION_UP:
                    touchIndicatorView.setVisibility(View.INVISIBLE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateFlipsTouchIndicatorPosition(touchIndicatorView, event);
                    break;
            }

            view.performClick();
            return detector.onTouchEvent(event);
        });
    }


    private void updateFlipsTouchIndicatorPosition(View touchIndicatorView, MotionEvent event) {
        int x = (int) event.getX() - (touchIndicatorView.getWidth() / 2);
        int y = (int) event.getY() - (touchIndicatorView.getHeight() / 2);

        touchIndicatorView.setX(x);
        touchIndicatorView.setY(y);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (ControllerUtils.checkDpadDevice(event)) {
            int keyCode = ControllerUtils.dpadAxisToKey(event);

            if (keyCode != -1) {
                controllerManager.precessKeyInput(keyCode, event);

                return true;
            }
        }

        if (ControllerUtils.checkGenericMotionEvent(event)) {
            for (int i = 0; i < event.getHistorySize(); i++) {
                controllerManager.processJoystickInput(event, i);
            }

            controllerManager.processJoystickInput(event, -1);

            return true;
        }

        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ControllerUtils.checkKeyDownEvent(event)) {

            if (controllerManager.precessKeyInput(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onTakeoffLandButtonClick(View view) {
        TelloAction.TAKEOFF_LAND.invoke(tello);
    }

    public void onChangePhotoVideoButtonClick(View view) {
        TelloAction.CHANGE_PHOTO_VIDEO.invoke(tello);
    }

    public void onChangeSpeedButtonClicked(View view) {
        TelloAction.CHANGE_SPEED.invoke(tello);
    }

    public void onVideoSettingsButtonClick(View view) {
        View icon = view.findViewById(R.id.videoSettingsImage);
        icon.animate().rotationBy(360).setDuration(500);

        new VideoSettingsWindow(this, tello).create();
    }

    public void onHomePageClick(View view) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_home, R.anim.slide_out_main);
    }

    public void updateExposureValue(int progress) {
        TextView textView = findViewById(R.id.exposureInfoText);
        textView.setText(VideoUtils.getExposureString(progress));
    }

    public void updateBitrateValue(int progress) {
        TextView textView = findViewById(R.id.bitrateInfoText);
        textView.setText(VideoUtils.getBitrateString(progress));
    }

    @Override
    public void onConnect() {
        tello.getVideoInfo().startVideoStream((int) (appSettings.getIFrameInterval() * 1000));
    }

    @Override
    public void onDisconnect() {
    }

    public Tello getTello() {
        return tello;
    }

    public void onButtonAI(View view) {
        if (!flightModeRunning) {
            Intent intent = new Intent(this, FlightModesActivity.class);
            startActivity(intent);
        } else {
            TelloAction.STOP_ALL.invoke(tello);
            setFlightModeRunning(false);
        }
    }

    public void setFlightModeRunning(boolean running) {
        if (running && !flightModeRunning) {
            findViewById(R.id.flightModesButton).setBackgroundResource(R.drawable.ic_round_cancel_24);
            flightModeRunning = true;
        } else if (!running && flightModeRunning) {
            findViewById(R.id.flightModesButton).setBackgroundResource(R.drawable.ic_round_smart_toy_48);
            flightModeRunning = false;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void onTakePhotoVideoClick(View view) {
        tello.getVideoInfo().takePicture();
    }
}