package me.cubixor.bettertello

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class VideoSettingsViewModel(videoViewModel: VideoViewModel = VideoViewModelImpl(App.getInstance().appSettingsRepository)) : ViewModel(),
    VideoViewModel by videoViewModel {

    private val appSettingsRepository = App.getInstance().appSettingsRepository

    val bitrate: LiveData<Int> = appSettingsRepository.bitrate
    val exposure: LiveData<Int> = appSettingsRepository.exposure
    val quality: LiveData<Boolean> = appSettingsRepository.quality
    val iFrameInterval: LiveData<Float> = appSettingsRepository.iFrameInterval

/*    @StringRes
    val bitrateRes: LiveData<Int> = bitrate.map { bitRate ->
        VideoUtils.getBitrateString(bitRate)
    }

    val exposureString: LiveData<String> = exposure.map { exposure ->
        VideoUtils.getExposureString(exposure)
    }*/

    @StringRes
    val qualityRes: LiveData<Int> = quality.map { quality ->
        if (quality) R.string.pic_quality_high else R.string.pic_quality_low
    }

    fun updateBitrate(bitrate: Int) {
        appSettingsRepository.updateBitrate(bitrate)
    }

    fun updateExposure(exposure: Int) {
        appSettingsRepository.updateExposure(exposure)
    }

    fun updateQuality(quality: Boolean) {
        appSettingsRepository.updateQuality(quality)
    }

    fun updateIFrameInterval(iFrameInterval: Float) {
        appSettingsRepository.updateIFrameInterval(iFrameInterval)
    }
}