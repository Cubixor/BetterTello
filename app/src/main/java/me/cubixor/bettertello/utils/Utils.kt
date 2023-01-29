package me.cubixor.bettertello.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import kotlin.math.roundToInt

object Utils {
    @JvmStatic
    fun dpToPixels(context: Context, dp: Int): Int {
        return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    fun fullScreen(activity: Activity) {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) // hide status bar and nav bar after a short delay, or if the user interacts with the middle of the screen
    }
}