package me.cubixor.bettertello;

import android.os.Bundle;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import me.cubixor.bettertello.controller.Controller;
import me.cubixor.bettertello.controller.ControllerUtils;
import me.cubixor.bettertello.data.AppSettings;
import me.cubixor.bettertello.tello.TelloAction;
import me.cubixor.bettertello.utils.KeyCodes;
import me.cubixor.bettertello.utils.Utils;

public class SettingsActivity extends AppCompatActivity {

    private View lastClickedView;
    private Fragment currentSection;
    private View lastClickedToSet;

    private AppSettings appSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Utils.fullScreen(this);

        Activities.updateSettingsActivity(this);
        appSettings = AppSettings.getInstance();
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (ControllerUtils.checkDpadDevice(event)) {
            int keyCode = ControllerUtils.dpadAxisToKey(event);

            if (keyCode != -1) {
                processInputEvent(keyCode, event);

                return true;
            }
        }

        if (ControllerUtils.checkGenericMotionEvent(event)) {
            //TODO Configurable axis
            return true;
        }

        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ControllerUtils.checkKeyDownEvent(event)) {
            if (processInputEvent(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean processInputEvent(int keyCode, InputEvent event) {
        if (currentSection instanceof FragmentSettingsController) {
            if (lastClickedToSet == null) {
                return true;
            }

            Controller controller = ControllerUtils.getControllerByID(event.getDevice().getDescriptor());

            if (!controller.equals(getSelectedController())) {
                return true;
            }

            TelloAction telloAction = getActionByView(lastClickedToSet);

            if (controller.getMappings().containsKey(keyCode)) {
                TelloAction toRemove = controller.getMappings().get(keyCode);
                Button toClear = findViewById(Utils.getViewByAction(toRemove)).findViewById(R.id.controllerButton);
                toClear.setText("");

                appSettings.removeControllerMapping(controller, toRemove);
            }

            appSettings.addControllerMapping(controller, telloAction, keyCode);

            Button button = lastClickedToSet.findViewById(R.id.controllerButton);
            button.setText(KeyCodes.keyCodes[keyCode]);
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_background, null));
            lastClickedToSet = null;


            return true;
        }
        return false;
    }


    public void onButtonChange(View view) {
        View parent = ((View) view.getParent());

        if (lastClickedToSet != null) {
            Button button = lastClickedToSet.findViewById(R.id.controllerButton);
            button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_background, null));

            if (lastClickedToSet.equals(parent)) {

                appSettings.removeControllerMapping(getSelectedController(), getActionByView(parent));
                button.setText("");
                lastClickedToSet = null;

                return;
            }
        }

        lastClickedToSet = parent;
        Button button = lastClickedToSet.findViewById(R.id.controllerButton);
        button.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_clicked_background, null));
    }

    public void onButtonRestore(View view) {
        for (TelloAction telloAction : TelloAction.values()) {
            Button button = findViewById(Utils.getViewByAction(telloAction)).findViewById(R.id.controllerButton);
            button.setText("");
            button.setBackgroundResource(R.drawable.button_background);
        }
        appSettings.resetMappings(getSelectedController());
    }

    public void onBackClick(View view) {
        finish();
    }

    public void onSectionClick(View view) {
        if (lastClickedView != null) {
            lastClickedView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_background, null));
        }
        lastClickedView = view;
        view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.button_clicked_background, null));

        openSection(view.getId());
    }

    public void openSection(int viewID) {
        Fragment newFragment = chooseSection(viewID);
        if (newFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.settingsFragmentView, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();

            currentSection = newFragment;
        }
    }

    private Fragment chooseSection(int viewID) {
        switch (viewID) {
            case R.id.settingsControllerButton: {
                if (appSettings.getControllers().isEmpty()) {
                    return new SettingsControllerEmptyFragment();
                } else {
                    return new FragmentSettingsController();
                }
            }
            case R.id.settingsGeneralButton:
                return new SettingsGeneralFragment();
        }
        return null;
    }

    private TelloAction getActionByView(View view) {
        switch (view.getId()) {
            case (R.id.controllerTakeoffLand):
                return TelloAction.TAKEOFF_LAND;
            case (R.id.controllerThrowTakeoffHandLand):
                return TelloAction.THROW_TAKEOFF_HAND_LAND;
            case (R.id.controllerStartMotors):
                return TelloAction.START_MOTORS;
            case (R.id.controllerChangeSpeed):
                return TelloAction.CHANGE_SPEED;
            case (R.id.controllerChangePhotoVideo):
                return TelloAction.CHANGE_PHOTO_VIDEO;
            case (R.id.controllerIncreaseExposure):
                return TelloAction.INCREASE_EXPOSURE;
            case (R.id.controllerDecreaseExposure):
                return TelloAction.DECREASE_EXPOSURE;
            case (R.id.controllerIncreaseBitrate):
                return TelloAction.INCREASE_BITRATE;
            case (R.id.controllerDecreaseBitrate):
                return TelloAction.DECREASE_BITRATE;
            case (R.id.controllerStop):
                return TelloAction.STOP_ALL;
            case (R.id.controllerUpAndAway):
                return TelloAction.MODE_UP_AND_AWAY;
            case (R.id.controllerCircle):
                return TelloAction.MODE_CIRCLE;
            case (R.id.controller360Video):
                return TelloAction.MODE_VIDEO_360;
            case (R.id.controllerBounce):
                return TelloAction.MODE_BOUNCE;
            case (R.id.controllerFlipsMenu):
                return TelloAction.MODE_8D_FLIPS;
            case (R.id.controllerFlipForward):
                return TelloAction.FLIP_FORWARD;
            case (R.id.controllerFlipForwardLeft):
                return TelloAction.FLIP_FORWARD_LEFT;
            case (R.id.controllerFlipForwardRight):
                return TelloAction.FLIP_FORWARD_RIGHT;
            case (R.id.controllerFlipLeft):
                return TelloAction.FLIP_LEFT;
            case (R.id.controllerFlipRight):
                return TelloAction.FLIP_RIGHT;
            case (R.id.controllerFlipBackward):
                return TelloAction.FLIP_BACKWARD;
            case (R.id.controllerFlipBackwardLeft):
                return TelloAction.FLIP_BACKWARD_LEFT;
            case (R.id.controllerFlipBackwardRight):
                return TelloAction.FLIP_BACKWARD_RIGHT;

        }
        return null;
    }

    private Controller getSelectedController() {
        Spinner spinner = findViewById(R.id.controllerTitle);
        return appSettings.getControllers().get(spinner.getSelectedItemPosition());
    }

    public Fragment getCurrentSection() {
        return currentSection;
    }
}