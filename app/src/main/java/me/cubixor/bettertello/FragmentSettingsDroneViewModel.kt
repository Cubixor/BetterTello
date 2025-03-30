package me.cubixor.bettertello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cubixor.telloapi.api.Tello
import javax.inject.Inject

@HiltViewModel
class FragmentSettingsDroneViewModel @Inject constructor(private val tello: Tello) : ViewModel() {

    private val _lowBatteryThreshold: MutableLiveData<Int> = MutableLiveData(tello.droneState.lowBatteryThreshold)
    val lowBatteryThreshold: LiveData<Int> = _lowBatteryThreshold

    val lowBatteryThresholdText: LiveData<String> = lowBatteryThreshold.map {
        "$it%"
    }

    private val _attitudeLimit: MutableLiveData<Int> = MutableLiveData(tello.droneState.maxAttitudeAngle.toInt())
    val attitudeLimit: LiveData<Int> = _attitudeLimit

    val attitudeLimitText: LiveData<String> = attitudeLimit.map {
        "$it°"
    }

    private val _heightLimit: MutableLiveData<Int> = MutableLiveData(tello.droneState.heightLimit)
    val heightLimit: LiveData<Int> = _heightLimit

    val heightLimitText: LiveData<String> = heightLimit.map {
        "${it}m"
    }

    private val _wifiSSID: MutableLiveData<String> = MutableLiveData(tello.droneState.wifiSSID)
    val wifiSSID: LiveData<String> = _wifiSSID

    private val _wifiPassword: MutableLiveData<String> = MutableLiveData(tello.droneState.wifiPassword)
    val wifiPassword: LiveData<String> = _wifiPassword


    fun updateLowBatteryThreshold(value: Int) {
        _lowBatteryThreshold.value = value
        tello.droneState.updateLowBatteryThreshold(value.toShort())
    }

    fun updateAttitudeLimit(value: Int) {
        _attitudeLimit.value = value
        tello.droneState.updateMaxAttitudeAnge(value.toFloat())
    }

    fun updateHeightLimit(value: Int) {
        _heightLimit.value = value
        tello.droneState.updateHeightLimit(value.toShort())
    }

    fun updateWiFiSSID(value: String) {
        _wifiSSID.value = value
        tello.droneState.updateWifiSSID(value)
    }

    fun updateWifiPassword(value: String) {
        _wifiPassword.value = value
        tello.droneState.updateWifiPassword(value)
    }
}