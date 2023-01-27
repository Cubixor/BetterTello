package me.cubixor.bettertello.tello


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.cubixor.bettertello.App
import me.cubixor.bettertello.bar.BarState
import me.cubixor.bettertello.bar.BarStateManager
import me.cubixor.telloapi.api.DroneStatus
import me.cubixor.telloapi.api.listeners.DroneConnectionListener
import me.cubixor.telloapi.api.listeners.DroneStatusListener
import me.cubixor.telloapi.api.video.SmartVideoMode

class TelloStateManager : DroneStatusListener, DroneConnectionListener {
    private val barStateManager: BarStateManager = App.getInstance().barStateManager

    private val wifiStrength = MutableStateFlow(0)
    fun observeWifiStrength(): Flow<Int> = wifiStrength.asStateFlow()
    private val batteryPercentage = MutableStateFlow(0)
    fun observeBatteryPercentage(): Flow<Int> = batteryPercentage.asStateFlow()

    private val smartModeRunning = MutableStateFlow(false)
    fun observeSmartModeRunning(): Flow<Boolean> = smartModeRunning.asStateFlow()
    private val flying = MutableStateFlow(false)
    fun observeFlying(): Flow<Boolean> = flying.asStateFlow()
    private val speed = MutableStateFlow(0)
    fun observeSpeed(): Flow<Int> = speed.asStateFlow()
    private val height = MutableStateFlow(0)
    fun observeHeight(): Flow<Int> = height.asStateFlow()
    private val flightTime = MutableStateFlow(0)
    fun observeFlightTime(): Flow<Int> = flightTime.asStateFlow()
    private val flipsMode = MutableStateFlow(false)
    fun observeFlipsMode(): Flow<Boolean> = flipsMode.asStateFlow()


    override fun onConnect() {
        App.getInstance().appSettingsRepository.startVideo()
        barStateManager.clearBars()
        barStateManager.addState(BarState.READY_TO_FLY)
    }

    override fun onDisconnect() {
        barStateManager.clearBars()
        barStateManager.addState(BarState.LOST_CONNECTION)
    }

    override fun onLightStrengthPacketReceive(lightOK: Boolean) {
        barStateManager.addRemoveState(BarState.LOW_LIGHT, !lightOK)
    }

    override fun onWifiStrengthPacketReceive(wifiStrength: Int, wifiInterference: Int) {
        this.wifiStrength.value = wifiStrength
        barStateManager.addRemoveState(BarState.WEAK_WIFI_SIGNAL, wifiStrength <= 40)
        barStateManager.addRemoveState(BarState.STRONG_WIFI_INTERFERENCE, wifiInterference > 0)
    }

    override fun onStatusPacketReceive(droneStatus: DroneStatus) {
        barStateManager.addRemoveState(BarState.LOW_BATTERY, droneStatus.isBatteryLow)
        barStateManager.addRemoveState(BarState.CRITICAL_BATTERY, droneStatus.isBatteryCritical)
        barStateManager.addRemoveState(BarState.BATTERY_ERROR, droneStatus.isBatteryError)
        barStateManager.addRemoveState(BarState.CAMERA_ERROR, droneStatus.isCameraError != 0)
        barStateManager.addRemoveState(BarState.DOWNWARD_VISION_ERROR, droneStatus.isDownwardVisionError)
        barStateManager.addRemoveState(BarState.GRAVITY_ERROR, droneStatus.isGravityError)
        barStateManager.addRemoveState(BarState.IMU_ERROR, droneStatus.isImuError)
        barStateManager.addRemoveState(BarState.OVERHEAT, droneStatus.isOverheat)
        barStateManager.addRemoveState(BarState.TOO_WINDY, droneStatus.isTooWindy)


        computeFlightTime(droneStatus.flyTime)
        flying.value = !droneStatus.iseMSky()
        batteryPercentage.value = droneStatus.batteryPercentage
        speed.value = droneStatus.flySpeed
        height.value = droneStatus.height

    }

    private var lastPacketTime: Long = 0
    private var lastPacketID = 0
    private fun computeFlightTime(flyTime: Int) {
        if (lastPacketID != flyTime) {
            flightTime.value = when {
                flyTime == 0 -> 0
                lastPacketID == flyTime - 1 -> (flightTime.value + (System.currentTimeMillis() - lastPacketTime)).toInt()
                else -> flightTime.value + ((flyTime - lastPacketID) * 100)

            }
            lastPacketID = flyTime
        }
        lastPacketTime = System.currentTimeMillis()
    }

    override fun onSmartVideoPacketReceive(smartVideoMode: SmartVideoMode, running: Boolean) {
        setSmartModeRunning(running)
    }

    fun setSmartModeRunning(running: Boolean) {
        smartModeRunning.value = running
    }

    fun switchFlipsMode() {
        setFlipsMode(!flipsMode.value)
    }

    fun setFlipsMode(enabled: Boolean) {
        flipsMode.value = enabled
        smartModeRunning.value = enabled
    }
}