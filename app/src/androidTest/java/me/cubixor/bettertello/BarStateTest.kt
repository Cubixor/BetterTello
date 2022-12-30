package me.cubixor.bettertello

import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.cubixor.bettertello.bar.BarState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BarStateTest {

    @get:Rule
    val activity = ActivityScenarioRule(HomePageActivity::class.java)


    @Test
    fun checkBarContents() {
        val barStateManager = App.getBarStateManager()

        barStateManager.addState(BarState.LOW_LIGHT)

        barStateManager.addState(BarState.LOW_BATTERY)

        barStateManager.addState(BarState.FLYING)

        barStateManager.addState(BarState.IMU_ERROR)

/*

        barStateManager.addState(BarState.CAMERA_ERROR)

        barStateManager.addState(BarState.WEAK_WIFI_SIGNAL)

        barStateManager.removeState(BarState.CAMERA_ERROR)

        barStateManager.removeState(BarState.WEAK_WIFI_SIGNAL)

        barStateManager.removeState(BarState.IMU_ERROR)

        barStateManager.removeState(BarState.LOW_BATTERY)

        barStateManager.removeState(BarState.FLYING)

        barStateManager.removeState(BarState.IMU_ERROR)*/

    }

}