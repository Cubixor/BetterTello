package me.cubixor.bettertello

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cubixor.bettertello.bar.BarStateManager
import me.cubixor.bettertello.data.AppSettingsRepository
import me.cubixor.bettertello.tello.TelloManager
import me.cubixor.bettertello.tello.TelloStateManager
import me.cubixor.telloapi.api.video.BitRate
import me.cubixor.telloapi.api.video.VideoInfo
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    appSettingsRepository: AppSettingsRepository,
    barStateManager: BarStateManager,
    stateManager: TelloStateManager,
    private val telloManager: TelloManager,
) : BarViewModel(barStateManager, stateManager) {


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

    val downloading: LiveData<Int> = stateManager.observePhotoDownloadPercentage().asLiveData()

    val timeFlying: LiveData<String> = stateManager.observeFlightTime().asLiveData().map {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("mm:ss")
        val instant = Instant.ofEpochMilli(it.toLong())
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
        val time = dateTimeFormatter.format(localDateTime)
        time
    }

    val height: LiveData<Int> = stateManager.observeHeight().asLiveData()

    val speed: LiveData<Int> = stateManager.observeSpeed().asLiveData()

    @DrawableRes
    val flyingRes: LiveData<Int> = stateManager.observeFlying().asLiveData().map {
        if (it) R.drawable.ic_round_flight_land_48
        else R.drawable.ic_round_flight_takeoff_48
    }

    @StringRes
    val iFastModeRes: LiveData<Int> =
        appSettingsRepository.isFastMode.map { fastMode ->
            if (fastMode) R.string.fast_letter
            else R.string.slow_letter
        }

    val smartModeRunning: LiveData<Boolean> = stateManager.observeSmartModeRunning().asLiveData()
    val flipsModeRunning: LiveData<Boolean> = stateManager.observeFlipsMode().asLiveData()

    @DrawableRes
    val smartModeRes: LiveData<Int> =
        smartModeRunning.map { running ->
            if (running) R.drawable.ic_round_cancel_24
            else R.drawable.ic_round_smart_toy_48
        }

    @DrawableRes
    val photoModeRes: LiveData<Int> =
        appSettingsRepository.isPhotoMode.map { photo ->
            if (photo) R.drawable.photo_btn
            else R.drawable.video_btn
        }

    fun onTakeoffLandButtonClick() {
        telloManager.takeoffLand()
    }

    fun onChangePhotoVideoButtonClick() {
        telloManager.changePhotoVideo()
    }

    fun onChangeSpeedButtonClicked() {
        telloManager.changeSpeed()
    }

    fun onTakePhotoVideoClick() {
        telloManager.takePhoto()
    }

    fun onButtonAiClick(stop: Boolean) {
        if (stop) {
            telloManager.stopAll()
        }
    }

    init {
        Log.d(TAG, "Injected: ${barStateManager.hashCode()}")
    }
}