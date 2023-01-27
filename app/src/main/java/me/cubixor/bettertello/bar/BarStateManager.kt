package me.cubixor.bettertello.bar

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BarStateManager(currentState: BarState = BarState.NOT_CONNECTED) {
    private val activeStates = MutableStateFlow(arrayListOf(currentState))
    private val currentBar = MutableStateFlow(currentState)
    private val stateChanges = MutableStateFlow(BarStateChange(currentState, true, 0))
    fun observeActiveStates(): Flow<ArrayList<BarState>> = activeStates.asStateFlow()
    fun observeCurrentBar(): Flow<BarState> = currentBar.asStateFlow()
    fun observeStateChanges(): Flow<BarStateChange> = stateChanges.asStateFlow()


    fun addRemoveState(state: BarState, add: Boolean) {
        if (activeStates.value.contains(state) && !add) {
            removeState(state)
        } else if (add) {
            addState(state)
        }
    }

    fun addState(toPut: BarState) {
        val activeStatesVal = ArrayList(activeStates.value)
        val currentBarVal = currentBar.value

        if (activeStatesVal.contains(toPut)) return

        var index: Int = activeStatesVal.size
        for (barState in ArrayList(activeStatesVal)) {
            if (barState.priority <= toPut.priority) {
                index = activeStatesVal.indexOf(barState)
                activeStatesVal.add(index, toPut)
                break
            }
        }
        if (!activeStatesVal.contains(toPut)) {
            activeStatesVal.add(toPut)
        }

        if (toPut.priority >= currentBarVal.priority) {
            changeBar(toPut)
        }

        activeStates.value = activeStatesVal
        stateChanges.value = BarStateChange(toPut, true, index)
    }

    fun removeState(toRemove: BarState) {
        val activeStatesVal = ArrayList(activeStates.value)
        val currentBarVal = currentBar.value

        if (!activeStatesVal.contains(toRemove)) return

        val index = activeStatesVal.indexOf(toRemove)

        activeStatesVal.remove(toRemove)
        if (currentBarVal == toRemove) {
            val nextBar: BarState = activeStatesVal.size.let {
                when {
                    it == 0 -> BarState.LOST_CONNECTION
                    it > index -> activeStatesVal[index]
                    else -> activeStatesVal[index - 1]
                }
            }
            changeBar(nextBar)
        }

        activeStates.value = activeStatesVal
        stateChanges.value = BarStateChange(toRemove, false, index)

    }

    private fun changeBar(barState: BarState) {
        currentBar.update { barState }
    }

    //TODO
    fun clearBars() {
        activeStates.value = arrayListOf()

    }

}