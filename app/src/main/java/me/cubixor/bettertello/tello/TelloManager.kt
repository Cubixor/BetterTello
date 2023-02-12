package me.cubixor.bettertello.tello

import me.cubixor.bettertello.data.AppSettingsRepository
import me.cubixor.telloapi.api.FlipDirection
import me.cubixor.telloapi.api.Tello
import me.cubixor.telloapi.api.video.SmartVideoMode
import javax.inject.Inject

class TelloManager @Inject constructor(
    private val appSettings: AppSettingsRepository,
    private val telloStateManager: TelloStateManager,
    private val tello: Tello
) {

    fun takeoffLand() {
        if (!tello.droneState.iseMSky()) {
            tello.takeOff()
        } else {
            tello.land(false)
        }
    }

    fun throwTakeoffHandLand() {
        if (!tello.droneState.iseMSky()) {
            tello.throwTakeOff()
        } else {
            tello.palmLand()
        }
    }

    fun startMotors() {
        tello.startMotors()
    }

    fun increaseExposure() {
        appSettings.updateExposure(appSettings.exposure.value!! + 1)
    }

    fun decreaseExposure() {
        appSettings.updateExposure(appSettings.exposure.value!! - 1)
    }

    fun increaseBitrate() {
        appSettings.updateBitrate(appSettings.bitrate.value!! + 1)
    }

    fun decreaseBitrate() {
        appSettings.updateBitrate(appSettings.bitrate.value!! - 1)
    }

    fun changeSpeed() {
        appSettings.switchFastMode()
    }

    fun changePhotoVideo() {
        appSettings.switchPhotoMode()
    }

    fun takePhoto() {
        tello.videoInfo.takePicture()
    }

    fun modeBounce() {
        stopAll()
        tello.bounceMode(true)
        telloStateManager.setSmartModeRunning(true)
    }

    fun mode8DFlips() {
        telloStateManager.switchFlipsMode()
    }

    fun handleOriginalFlightMode(smartVideoMode: SmartVideoMode) {
        val stop = tello.videoInfo.isSmartVideoRunning && tello.videoInfo.smartVideoMode == smartVideoMode
        stopAll()
        if (!stop) {
            tello.videoInfo.toggleSmartVideo(smartVideoMode, true)
        }
    }


    fun flip(direction: FlipDirection?) {
        if (tello.droneState.batteryPercentage > 50) {
            tello.flip(direction)
        }
    }

    fun stopAll() {
        if (tello.videoInfo.isSmartVideoRunning) {
            val smartVideoMode = tello.videoInfo.smartVideoMode
            tello.videoInfo.toggleSmartVideo(smartVideoMode, false)
        }
        tello.bounceMode(false)
        telloStateManager.setSmartModeRunning(false)
        telloStateManager.setFlipsMode(false)
    }
}