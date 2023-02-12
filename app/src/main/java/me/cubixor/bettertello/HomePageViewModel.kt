package me.cubixor.bettertello

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.cubixor.bettertello.bar.BarState
import me.cubixor.bettertello.bar.BarStateChange
import me.cubixor.bettertello.bar.BarStateManager
import me.cubixor.bettertello.tello.TelloStateManager
import javax.inject.Inject

private const val TAG = "HomePageViewModel"

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val barStateManager: BarStateManager,
    telloStateManager: TelloStateManager
) : BarViewModel(barStateManager, telloStateManager) {


    private val _expandButtonShown: MutableLiveData<Boolean> = MutableLiveData(false)
    val expandButtonShown: LiveData<Boolean> = _expandButtonShown

    private val _statesExpanded: MutableLiveData<Boolean> = MutableLiveData(false)
    val statesExpanded: LiveData<Boolean> = _statesExpanded

    private val _activeStates: MutableLiveData<ArrayList<BarState>> = MutableLiveData(arrayListOf())
    val activeStates: LiveData<ArrayList<BarState>> = _activeStates

    private val _addedStates = MutableLiveData<BarStateChange>()
    val addedStates: LiveData<BarStateChange> = _addedStates

    private val _removedStates = MutableLiveData<BarStateChange>()
    val removedStates: LiveData<BarStateChange> = _removedStates

    fun expandStates() {
        _statesExpanded.value = true
    }

    fun shrinkStates() {
        _statesExpanded.value = false
    }

    private suspend fun observeActiveStates() {
        barStateManager.observeActiveStates().collect { activeStates ->
            _activeStates.value = activeStates

            val buttonShown = expandButtonShown.value!!

            if (buttonShown && activeStates.size <= 1) {
                _expandButtonShown.value = false
                if (statesExpanded.value!!) {
                    _statesExpanded.value = false
                }
            } else if (!buttonShown && activeStates.size > 1) {
                _expandButtonShown.value = true
            }
        }
    }

    init {
        Log.d(TAG, "Injected: ${barStateManager.hashCode()}")

        viewModelScope.launch {
            observeActiveStates()
        }

        viewModelScope.launch {
            barStateManager.observeStateChanges().collect {
                if (it.add)
                    _addedStates.value = it
                else
                    _removedStates.value = it
            }
        }
    }


}