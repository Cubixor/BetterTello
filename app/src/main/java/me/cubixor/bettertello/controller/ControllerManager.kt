package me.cubixor.bettertello.controller

import android.content.Context
import android.hardware.input.InputManager
import android.hardware.input.InputManager.InputDeviceListener
import android.view.InputDevice
import android.view.InputEvent
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import me.cubixor.bettertello.App
import me.cubixor.bettertello.R
import me.cubixor.bettertello.data.AppSettingsRepository
import me.cubixor.bettertello.tello.TelloManager
import me.cubixor.telloapi.api.Tello
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class ControllerManager @Inject constructor(
    private val tello: Tello,
    private val telloManager: TelloManager,
    private val appSettings: AppSettingsRepository
) : InputDeviceListener {

    private val inputManager: InputManager = App.instance.getSystemService(Context.INPUT_SERVICE) as InputManager

    init {
        inputManager.registerInputDeviceListener(this, null)
        loadControllers()
    }

    private fun loadControllers() {
        val devices = getConnectedInputDevices()
        for (inputDevice in devices) {
            val controller = getControllerByID(inputDevice.descriptor)
            if (controller == null) {
                appSettings.addController(inputDevice)
            }
        }
    }

    fun getFirstController(): Controller {
        for (inputDevice in getConnectedInputDevices()) {
            val controller = getControllerByID(inputDevice.descriptor)
            if (controller != null) {
                return controller
            }
        }
        return appSettings.controllers.first()
    }

    fun getControllerByID(descriptor: String): Controller? {
        for (controller in appSettings.controllers) {
            if (controller.descriptor == descriptor) {
                return controller
            }
        }
        return null
    }

    private fun getConnectedInputDevices(): List<InputDevice> {
        val devices = mutableListOf<InputDevice>()
        val deviceIds = InputDevice.getDeviceIds()

        for (deviceId in deviceIds) {
            val dev = InputDevice.getDevice(deviceId)!!
            if (checkInputDevice(dev.sources) && !devices.contains(dev)) devices.add(dev)
        }
        return devices
    }

    fun onGenericMotionEvent(
        event: MotionEvent, processKeyInput: (Int, InputEvent) -> Boolean, processJoystickInput: (MotionEvent, Int) -> Unit = { _, _ -> }
    ): Boolean {
        if (checkDpadDevice(event)) {
            val keyCode = dpadAxisToKey(event)
            if (keyCode != -1) {
                processKeyInput.invoke(keyCode, event)
                return true
            }
        }
        if (checkGenericMotionEvent(event)) {
            for (i in 0 until event.historySize) {
                processJoystickInput.invoke(event, i)
            }
            processJoystickInput.invoke(event, -1)
            return true
        }
        return false
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent, processKeyInput: (Int, InputEvent) -> Boolean): Boolean {
        return if (checkKeyDownEvent(event)) {
            processKeyInput.invoke(keyCode, event)
        } else false
    }

    fun processJoystickInput(event: MotionEvent, historyPos: Int) {
        val inputDevice = event.device
        val yaw = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos)
        val roll = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos)
        val throttle = -getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos)
        val pitch = -getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos)
        tello.droneAxis.setAxis(roll, pitch, throttle, yaw)
    }

    fun processKeyInput(keyCode: Int, event: InputEvent): Boolean {
        val controller = getControllerByID(event.device.descriptor)
        val telloAction = controller?.mappings?.get(keyCode)
        if (telloAction != null) {
            telloAction.invoke(telloManager)
            return true
        }
        return false
    }


    private fun getCenteredAxis(event: MotionEvent, device: InputDevice, axis: Int, historyPos: Int): Float {
        val range = device.getMotionRange(axis, event.source)

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            val flat = range.flat
            val value = if (historyPos < 0) event.getAxisValue(axis) else event.getHistoricalAxisValue(axis, historyPos)

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (abs(value) > flat) {
                return value
            }
        }
        return 0f
    }

    private fun dpadAxisToKey(motionEvent: MotionEvent): Int {
        val xAxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X)
        val yAxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y)
        return when {
            xAxis.compareTo(-1.0f) == 0 -> KeyEvent.KEYCODE_DPAD_LEFT
            xAxis.compareTo(1.0f) == 0 -> KeyEvent.KEYCODE_DPAD_RIGHT
            yAxis.compareTo(-1.0f) == 0 -> KeyEvent.KEYCODE_DPAD_UP
            yAxis.compareTo(1.0f) == 0 -> KeyEvent.KEYCODE_DPAD_DOWN
            else -> -1
        }
    }

    private fun checkGenericMotionEvent(event: MotionEvent): Boolean =
        event.source and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK && event.action == MotionEvent.ACTION_MOVE


    private fun checkKeyDownEvent(event: KeyEvent): Boolean =
        event.source and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD || event.source and InputDevice.SOURCE_DPAD == InputDevice.SOURCE_DPAD && event.repeatCount == 0


    private fun checkDpadDevice(event: InputEvent): Boolean = event.source and InputDevice.SOURCE_DPAD != InputDevice.SOURCE_DPAD

    private fun checkInputDevice(sources: Int): Boolean =
        sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD
                || sources and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK
                || sources and InputDevice.SOURCE_DPAD == InputDevice.SOURCE_DPAD


    override fun onInputDeviceAdded(deviceId: Int) {
        //Check if device is a controller
        val inputDevice = inputManager.getInputDevice(deviceId)!!
        if (!checkInputDevice(inputDevice.sources)) return


        //Save controller to settings if it wasn't used before.
        val controller = getControllerByID(inputDevice.descriptor)
        if (controller == null) {
            appSettings.addController(inputDevice)
        }

        //Send a Toast with device connected message
        val context = App.instance.applicationContext
        Toast.makeText(
            context, context.getString(R.string.controller_connected, inputDevice.name), Toast.LENGTH_SHORT
        ).show()


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

    override fun onInputDeviceRemoved(deviceId: Int) {}
    override fun onInputDeviceChanged(deviceId: Int) {}
}