package me.cubixor.bettertello.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

import me.cubixor.bettertello.Activities;
import me.cubixor.bettertello.HomePageActivity;
import me.cubixor.bettertello.MainActivity;
import me.cubixor.bettertello.R;
import me.cubixor.bettertello.tello.TelloAction;

public class Utils {


    public static int dpToPixels(Context context, int dp) {
        return Math.round(dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String getStr(int id) {
        return MainActivity.getActivity().getString(id);
    }

    public static String getStr(String aString) {
        String packageName = MainActivity.getActivity().getPackageName();
        int resId = MainActivity.getActivity().getResources().getIdentifier(aString, "string", packageName);
        return getStr(resId);
    }

    public static void fullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // hide status bar and nav bar after a short delay, or if the user interacts with the middle of the screen
    }

    public static void exit() {
        HomePageActivity homePageActivity = Activities.getHomePage();
        if (homePageActivity != null) {
            homePageActivity.finish();
        }
        MainActivity.getActivity().finish();
    }

    public static int getViewByAction(TelloAction telloAction) {

        switch (telloAction) {
            case TAKEOFF_LAND:
                return R.id.controllerTakeoffLand;
            case THROW_TAKEOFF_HAND_LAND:
                return R.id.controllerThrowTakeoffHandLand;
            case START_MOTORS:
                return R.id.controllerStartMotors;
            case CHANGE_SPEED:
                return R.id.controllerChangeSpeed;
            case CHANGE_PHOTO_VIDEO:
                return R.id.controllerChangePhotoVideo;
            case INCREASE_EXPOSURE:
                return R.id.controllerIncreaseExposure;
            case DECREASE_EXPOSURE:
                return R.id.controllerDecreaseExposure;
            case INCREASE_BITRATE:
                return R.id.controllerIncreaseBitrate;
            case DECREASE_BITRATE:
                return R.id.controllerDecreaseBitrate;
            case STOP_ALL:
                return R.id.controllerStop;
            case MODE_UP_AND_AWAY:
                return R.id.controllerUpAndAway;
            case MODE_CIRCLE:
                return R.id.controllerCircle;
            case MODE_VIDEO_360:
                return R.id.controller360Video;
            case MODE_BOUNCE:
                return R.id.controllerBounce;
            case MODE_8D_FLIPS:
                return R.id.controllerFlipsMenu;
            case FLIP_FORWARD:
                return R.id.controllerFlipForward;
            case FLIP_FORWARD_LEFT:
                return R.id.controllerFlipForwardLeft;
            case FLIP_FORWARD_RIGHT:
                return R.id.controllerFlipForwardRight;
            case FLIP_LEFT:
                return R.id.controllerFlipLeft;
            case FLIP_RIGHT:
                return R.id.controllerFlipRight;
            case FLIP_BACKWARD:
                return R.id.controllerFlipBackward;
            case FLIP_BACKWARD_LEFT:
                return R.id.controllerFlipBackwardLeft;
            case FLIP_BACKWARD_RIGHT:
                return R.id.controllerFlipBackwardRight;
        }

        return 0;
    }
}
