package me.cubixor.bettertello

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import me.cubixor.bettertello.tello.TelloAction
import me.cubixor.bettertello.utils.VideoUtils
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class MainActivityViewModel : BarViewModel() {

    private val stateManager = App.getInstance().telloStateManager
    private val appSettingsRepository = App.getInstance().appSettingsRepository
    private val tello = App.getInstance().tello

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

    @StringRes
    val bitRate: LiveData<Int> = appSettingsRepository.bitrate.map { bitRate ->
        VideoUtils.getBitrateString(bitRate)
    }

    val exposure: LiveData<String> = appSettingsRepository.exposure.map { exposure ->
        VideoUtils.getExposureString(exposure)
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
        TelloAction.TAKEOFF_LAND.invoke(tello)
    }

    fun onChangePhotoVideoButtonClick() {
        TelloAction.CHANGE_PHOTO_VIDEO.invoke(tello)
    }

    fun onChangeSpeedButtonClicked() {
        TelloAction.CHANGE_SPEED.invoke(tello)
    }

    fun onTakePhotoVideoClick() {
        TelloAction.TAKE_PHOTO.invoke(tello)
    }

    fun onButtonAiClick(stop: Boolean) {
        if (stop) {
            TelloAction.STOP_ALL.invoke(tello)
        }
    }
}