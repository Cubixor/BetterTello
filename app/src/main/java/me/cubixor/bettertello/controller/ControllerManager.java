package me.cubixor.bettertello.controller;

import static me.cubixor.bettertello.controller.ControllerUtils.getCenteredAxis;

import android.content.Context;
import android.hardware.input.InputManager;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import me.cubixor.bettertello.App;
import me.cubixor.bettertello.R;
import me.cubixor.bettertello.data.AppSettings;
import me.cubixor.bettertello.tello.TelloAction;
import me.cubixor.bettertello.utils.Utils;
import me.cubixor.telloapi.api.Tello;

public class ControllerManager implements InputManager.InputDeviceListener {

    private final Tello tello;
    private final AppSettings appSettings;
    private final InputManager inputManager;


    public ControllerManager() {
        tello = App.getInstance().getTello();
        appSettings = AppSettings.getInstance();
        inputManager = (InputManager) App.getInstance().getSystemService(Context.INPUT_SERVICE);
        inputManager.registerInputDeviceListener(this, null);
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if (ControllerUtils.checkDpadDevice(event)) {
            int keyCode = ControllerUtils.dpadAxisToKey(event);

            if (keyCode != -1) {
                precessKeyInput(keyCode, event);

                return true;
            }
        }

        if (ControllerUtils.checkGenericMotionEvent(event)) {
            for (int i = 0; i < event.getHistorySize(); i++) {
                processJoystickInput(event, i);
            }

            processJoystickInput(event, -1);

            return true;
        }

        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ControllerUtils.checkKeyDownEvent(event)) {
            return precessKeyInput(keyCode, event);
        }
        return false;
    }

    public void processJoystickInput(MotionEvent event, int historyPos) {
        InputDevice inputDevice = event.getDevice();

        float yaw = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos);
        float roll = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos);
        float throttle = -getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos);
        float pitch = -getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos);

        tello.getDroneAxis().setAxis(roll, pitch, throttle, yaw);
        //System.out.println("axis " + Arrays.toString(tello.getAxis()));
    }

    public boolean precessKeyInput(int keyCode, InputEvent event) {
        Controller controller = ControllerUtils.getControllerByID(event.getDevice().getDescriptor());
        TelloAction telloAction = controller.getMappings().get(keyCode);
        if (telloAction != null) {
            telloAction.invoke(tello);
            return true;
        }

        return false;
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {
        //Check if device is a controller
        InputDevice inputDevice = inputManager.getInputDevice(deviceId);
        if (!ControllerUtils.checkInputDevice(inputDevice.getSources())) {
            return;
        }

        //Save controller to settings if it wasn't used before.
        Controller controller = ControllerUtils.getControllerByID(inputDevice.getDescriptor());
        if (controller == null) {
            appSettings.addController(inputDevice);
        }

        //Send a Toast with device connected message
        Toast.makeText(App.getInstance().getApplicationContext(),
                Utils.getStr(R.string.controller_connected, inputDevice.getName()),
                Toast.LENGTH_SHORT).show();


        //Check if settings are open, update controllers section
/*
        SettingsActivity settingsActivity = Activities.getSettings();
        if (settingsActivity != null) {
            if (settingsActivity.getCurrentSection() instanceof SettingsControllerEmptyFragment) {
                settingsActivity.openSection(R.id.settingsControllerButton);
            } else if (settingsActivity.getCurrentSection() instanceof FragmentSettingsController) {
                FragmentSettingsController fragmentSettingsController = Activities.getSettingsControllerFragment();

                if (fragmentSettingsController != null) {
                    fragmentSettingsController.updateSpinner(controller.getInAppID());
                }
            }
        }
*/
    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {
    }
}
