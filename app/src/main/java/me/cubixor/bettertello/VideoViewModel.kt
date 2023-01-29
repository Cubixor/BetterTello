package me.cubixor.bettertello

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import me.cubixor.bettertello.data.AppSettingsRepository
import me.cubixor.telloapi.api.video.BitRate
import me.cubixor.telloapi.api.video.VideoInfo

interface VideoViewModel {

    val bitrateRes: LiveData<Int>
    val exposureStr: LiveData<String>
}

open class VideoViewModelImpl(appSettingsRepository: AppSettingsRepository) : VideoViewModel {

    @StringRes
    override val bitrateRes: LiveData<Int> = appSettingsRepository.bitrate.map { bitRate ->
        when (BitRate.values()[bitRate]) {
            BitRate.MBPS_1 -> R.string.bitrate_1
            BitRate.MBPS_1_5 -> R.string.bitrate_1_5
            BitRate.MBPS_2 -> R.string.bitrate_2
            BitRate.MBPS_3 -> R.string.bitrate_3
            BitRate.MBPS_4 -> R.string.bitrate_4
            else -> R.string.bitrate_auto
        }
    }

    override val exposureStr: LiveData<String> = appSettingsRepository.exposure.map { exposure ->
        val exposureFloat = VideoInfo.getExposureValues()[exposure]
        if (exposureFloat == 0.0f) App.getInstance().res.getString(R.string.exposure_auto) else exposureFloat.toString()
    }

}