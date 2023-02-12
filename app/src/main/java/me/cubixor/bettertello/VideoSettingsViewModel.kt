package me.cubixor.bettertello

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cubixor.bettertello.data.AppSettingsRepository
import me.cubixor.telloapi.api.video.BitRate
import me.cubixor.telloapi.api.video.VideoInfo
import javax.inject.Inject

@HiltViewModel
class VideoSettingsViewModel @Inject constructor(private val appSettingsRepository: AppSettingsRepository) : ViewModel() {


    val bitrate: LiveData<Int> = appSettingsRepository.bitrate
    val exposure: LiveData<Int> = appSettingsRepository.exposure
    val quality: LiveData<Boolean> = appSettingsRepository.quality
    val iFrameInterval: LiveData<Float> = appSettingsRepository.iFrameInterval

    @StringRes
    val bitrateRes: LiveData<Int> = appSettingsRepository.bitrate.map { bitRate ->
        when (BitRate.values()[bitRate]) {
            BitRate.MBPS_1 -> R.string.bitrate_1
            BitRate.MBPS_1_5 -> R.string.bitrate_1_5
            BitRate.MBPS_2 -> R.string.bitrate_2
            BitRate.MBPS_3 -> R.string.bitrate_3
            BitRate.MBPS_4 -> R.string.bitrate_4
            else -> R.string.bitrate_auto
        }
    }

    val exposureStr: LiveData<String> = appSettingsRepository.exposure.map { exposure ->
        val exposureFloat = VideoInfo.getExposureValues()[exposure]
        if (exposureFloat == 0.0f) App.instance.resources.getString(R.string.exposure_auto) else exposureFloat.toString()
    }

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