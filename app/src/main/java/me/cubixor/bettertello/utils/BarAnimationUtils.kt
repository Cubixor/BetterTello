package me.cubixor.bettertello.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import me.cubixor.bettertello.R
import me.cubixor.bettertello.bar.BarState

object BarAnimationUtils {
    fun changeText(context: Context, to: BarState, textView: TextView) {
        val out = AnimationUtils.loadAnimation(context, R.anim.slide_out_text)
        val `in` = AnimationUtils.loadAnimation(context, R.anim.slide_in_text)
        textView.startAnimation(out)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            textView.text = context.getString(to.textPath)
            textView.startAnimation(`in`)
        }, 300)
    }
}