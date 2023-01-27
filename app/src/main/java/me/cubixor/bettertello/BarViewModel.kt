package me.cubixor.bettertello

import androidx.annotation.DrawableRes
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.cubixor.bettertello.bar.BarState

open class BarViewModel : ViewModel() {

    private val stateManager = App.getInstance().telloStateManager
    private val barStateManager = App.getInstance().barStateManager

    @DrawableRes
    private val _wifiStrengthRes: MutableLiveData<Int> =
        MutableLiveData(R.drawable.ic_round_signal_wifi_0_bar_24)

    @DrawableRes
    val wifiStrengthRes: LiveData<Int> = _wifiStrengthRes

    val battery: LiveData<Int> = stateManager.observeBatteryPercentage().asLiveData()

    val currentBar: LiveData<BarState> = barStateManager.observeCurrentBar().asLiveData()

    private suspend fun collectWifiStrength() {
        stateManager.observeWifiStrength().collect {
            _wifiStrengthRes.value = when {
                it <= 20 -> R.drawable.ic_round_signal_wifi_0_bar_24
                it <= 40 -> R.drawable.ic_round_network_wifi_1_bar_24
                it <= 60 -> R.drawable.ic_round_network_wifi_2_bar_24
                it <= 80 -> R.drawable.ic_round_network_wifi_3_bar_24
                else -> R.drawable.ic_round_signal_wifi_4_bar_24
            }
        }
    }

    init {
        viewModelScope.launch {
            collectWifiStrength()
        }
    }
}