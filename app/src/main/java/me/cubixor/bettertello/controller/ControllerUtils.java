package me.cubixor.bettertello.controller;

import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import me.cubixor.bettertello.App;

public class ControllerUtils {


/*    public static ArrayList<Integer> getGameControllerIds() {
        ArrayList<Integer> gameControllerDeviceIds = new ArrayList<>();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                // This device is a game controller. Store its device ID.
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    gameControllerDeviceIds.add(deviceId);
                }
            }
        }
        return gameControllerDeviceIds;
    } */

    public static List<InputDevice> getConnectedInputDevices() {
        List<InputDevice> devices = new ArrayList<>();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || (((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK))) {

                if (!devices.contains(dev)) {
                    devices.add(dev);
                }
            }
        }
        return devices;
    }

    public static Controller getControllerByID(String descriptor) {
        for (Controller controller : App.getInstance().getAppSettingsRepository().getControllers()) {
            if (controller.getDescriptor().equals(descriptor)) {
                return controller;
            }
        }
        return null;
    }

    public static boolean checkGenericMotionEvent(MotionEvent event) {
        return (event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
                && event.getAction() == MotionEvent.ACTION_MOVE;
    }

    public static boolean checkKeyDownEvent(KeyEvent event) {
        return ((event.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                || ((event.getSource() & InputDevice.SOURCE_DPAD) != InputDevice.SOURCE_DPAD)
                && event.getRepeatCount() == 0;
    }

    public static boolean checkInputDevice(int sources) {
        return ((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                || (((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)
                || ((sources & InputDevice.SOURCE_DPAD) != InputDevice.SOURCE_DPAD));
    }

    public static boolean checkDpadDevice(InputEvent event) {
        return (event.getSource() & InputDevice.SOURCE_DPAD) == InputDevice.SOURCE_DPAD;
    }

    public static int dpadAxisToKey(MotionEvent motionEvent) {
        float xAxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
        float yAxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);

        if (Float.compare(xAxis, -1.0f) == 0) {
            return KeyEvent.KEYCODE_DPAD_LEFT;
        } else if (Float.compare(xAxis, 1.0f) == 0) {
            return KeyEvent.KEYCODE_DPAD_RIGHT;
        } else if (Float.compare(yAxis, -1.0f) == 0) {
            return KeyEvent.KEYCODE_DPAD_UP;
        } else if (Float.compare(yAxis, 1.0f) == 0) {
            return KeyEvent.KEYCODE_DPAD_DOWN;
        } else {
            return -1;
        }
    }

    public static float getCenteredAxis(MotionEvent event,
                                        InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range =
                device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value =
                    historyPos < 0 ? event.getAxisValue(axis) :
                            event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

    public static Controller selectController() {
        for (InputDevice inputDevice : ControllerUtils.getConnectedInputDevices()) {
            Controller controller = ControllerUtils.getControllerByID(inputDevice.getDescriptor());
            if (controller != null) {
                return controller;
            }
        }
        return null;
    }
}
