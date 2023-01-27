package me.cubixor.bettertello.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.cubixor.bettertello.App
import me.cubixor.telloapi.api.video.BitRate
import me.cubixor.telloapi.api.video.VideoMode

class AppSettingsRepository(private val appSettings: AppSettings) {

    private val tello = App.getInstance().tello

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

}