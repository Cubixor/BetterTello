package me.cubixor.bettertello.data

import android.view.InputDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.cubixor.bettertello.controller.Controller
import me.cubixor.bettertello.tello.TelloAction
import me.cubixor.telloapi.api.Tello
import me.cubixor.telloapi.api.video.BitRate
import me.cubixor.telloapi.api.video.VideoMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsRepository @Inject constructor(private val appSettings: AppSettings, private val tello: Tello) {

    private val _exposure: MutableLiveData<Int> = MutableLiveData(appSettings.exposure)
    val exposure: LiveData<Int> = _exposure

    private val _bitrate: MutableLiveData<Int> = MutableLiveData(appSettings.bitrate)
    val bitrate: LiveData<Int> = _bitrate

    private val _quality: MutableLiveData<Boolean> = MutableLiveData(appSettings.quality)
    val quality: LiveData<Boolean> = _quality

    private val _iFrameInterval: MutableLiveData<Float> = MutableLiveData(appSettings.iFrameInterval)
    val iFrameInterval: LiveData<Float> = _iFrameInterval

    private val _isFastMode: MutableLiveData<Boolean> = MutableLiveData(appSettings.isFastMode)
    val isFastMode: LiveData<Boolean> = _isFastMode

    private val _isPhotoMode: MutableLiveData<Boolean> = MutableLiveData(appSettings.isPhotoMode)
    val isPhotoMode: LiveData<Boolean> = _isPhotoMode

    val controllers: MutableList<Controller> = appSettings.loadControllers() as MutableList<Controller>

    fun startVideo() {
        tello.videoInfo.startVideoStream((iFrameInterval.value!! * 1000f).toInt())
    }

    fun updateExposure(exposure: Int) {
        if (exposure !in 0..18) {
            return
        }

        _exposure.value = exposure
        appSettings.exposure = exposure

        tello.videoInfo.setExposure(exposure - 9)
    }

    fun updateBitrate(bitrate: Int) {
        if (bitrate !in 0..5) {
            return
        }

        _bitrate.value = bitrate
        appSettings.bitrate = bitrate

        tello.videoInfo.bitRate = BitRate.values()[bitrate]
    }

    fun updateQuality(quality: Boolean) {
        _quality.value = quality
        appSettings.quality = quality

        tello.videoInfo.setJPEGQuality(quality)
    }

    fun updateIFrameInterval(iFrameInterval: Float) {
        _iFrameInterval.value = iFrameInterval
        appSettings.iFrameInterval = iFrameInterval

        tello.videoInfo.iFrameInterval = (iFrameInterval * 1000).toInt()
    }

    fun switchFastMode() {
        updateFastMode(!isFastMode.value!!)
    }

    fun updateFastMode(fastMode: Boolean) {
        _isFastMode.value = fastMode
        appSettings.isFastMode = fastMode

        tello.droneAxis.isFastMode = fastMode
    }

    fun switchPhotoMode() {
        updatePhotoMode(!isPhotoMode.value!!)
    }

    fun updatePhotoMode(photoMode: Boolean) {
        _isPhotoMode.value = photoMode
        appSettings.isPhotoMode = photoMode

        tello.videoInfo.videoMode = if (photoMode) VideoMode.PHOTO else VideoMode.VIDEO
    }

    fun addController(inputDevice: InputDevice) {
        val controller = Controller(inputDevice.descriptor, inputDevice.name, controllers.size + 1)
        controllers.add(controller)
        appSettings.saveControllers(controllers)
    }

    fun addControllerMapping(controller: Controller, telloAction: TelloAction, keyCode: Int) {
        controller.mappings[keyCode] = telloAction
        appSettings.saveControllers(controllers)
    }

    fun removeControllerMapping(controller: Controller, telloAction: TelloAction) {
        controller.mappings.remove(controller.getKeyByAction(telloAction))
        appSettings.saveControllers(controllers)
    }

    fun resetMappings(controller: Controller) {
        controller.mappings.clear()
        appSettings.saveControllers(controllers)
    }
}