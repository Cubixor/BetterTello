package me.cubixor.bettertello

import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import me.cubixor.bettertello.bar.BarState
import me.cubixor.bettertello.bar.BarStateManager
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class BarStateTest {

    @get:Rule
    val activity = ActivityScenarioRule(HomePageActivity::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var barStateManager: BarStateManager
    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun checkBarContents() {

        barStateManager.addState(BarState.LOW_LIGHT)
        Thread.sleep(2000)
        barStateManager.addState(BarState.LOW_BATTERY)
        Thread.sleep(2000)
        barStateManager.addState(BarState.FLYING)
        Thread.sleep(2000)
        barStateManager.addState(BarState.IMU_ERROR)
        Thread.sleep(4000)
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