package me.cubixor.bettertello

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.cubixor.bettertello.bar.BarState
import me.cubixor.bettertello.bar.BarStateChange
import me.cubixor.bettertello.bar.BarStateManager

class HomePageViewModel : ViewModel() {

    private val barStateManager: BarStateManager = App.getInstance().barStateManager

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

    val currentBar: LiveData<BarState> = barStateManager.observeCurrentBar().asLiveData()

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