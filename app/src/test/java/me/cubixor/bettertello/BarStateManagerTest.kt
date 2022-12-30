package me.cubixor.bettertello

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.cubixor.bettertello.bar.BarState
import me.cubixor.bettertello.bar.BarStateChange
import me.cubixor.bettertello.bar.BarStateManager
import org.junit.Test

class BarStateManagerTest {

    @ExperimentalCoroutinesApi
    @Test
    fun addStates() = runTest {
        val barStateManager = BarStateManager()

        barStateManager.addState(BarState.BATTERY_ERROR)
        barStateManager.addState(BarState.FLYING)
        barStateManager.addState(BarState.LOW_LIGHT)

        val activeStates = barStateManager.observeActiveStates().first()
        println(activeStates)

        assert(
            activeStates == arrayListOf(
                BarState.BATTERY_ERROR,
                BarState.LOW_LIGHT,
                BarState.FLYING,
                BarState.NOT_CONNECTED
            )
        )

    }

    @ExperimentalCoroutinesApi
    @Test
    fun removeStates() = runTest {
        val barStateManager = BarStateManager()

        barStateManager.removeState(BarState.NOT_CONNECTED)

        val activeStates = barStateManager.observeActiveStates().first()
        println(activeStates)

        assert(activeStates == arrayListOf<BarState>())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addRemoveStates() = runTest {
        val barStateManager = BarStateManager()

        barStateManager.addRemoveState(BarState.DOWNWARD_VISION_ERROR, true)
        barStateManager.addRemoveState(BarState.NOT_CONNECTED, false)
        barStateManager.addRemoveState(BarState.IMU_ERROR, true)
        barStateManager.addRemoveState(BarState.FLYING, true)
        barStateManager.addRemoveState(BarState.LOW_LIGHT, true)
        barStateManager.addRemoveState(BarState.IMU_ERROR, false)


        val activeStates = barStateManager.observeActiveStates().first()
        println(activeStates)

        assert(
            activeStates == arrayListOf(
                BarState.DOWNWARD_VISION_ERROR,
                BarState.LOW_LIGHT,
                BarState.FLYING
            )
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun removedStates() = runTest {
        val barStateManager = BarStateManager()

        val changedStatesFlow = barStateManager.observeStateChanges()
        val changes = mutableListOf<BarStateChange>()

        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            changedStatesFlow.toList(changes)
        }

        barStateManager.addState(BarState.FLYING)
        barStateManager.removeState(BarState.FLYING)
        barStateManager.addState(BarState.FLYING)
        barStateManager.removeState(BarState.FLYING)

        collectJob.cancel()

        println(changes)

        assert(
            changes == listOf(
                BarStateChange(BarState.NOT_CONNECTED, true, 0),
                BarStateChange(BarState.FLYING, true, 0),
                BarStateChange(BarState.FLYING, false, 0),
                BarStateChange(BarState.FLYING, true, 0),
                BarStateChange(BarState.FLYING, false, 0)
            )
        )

    }
}