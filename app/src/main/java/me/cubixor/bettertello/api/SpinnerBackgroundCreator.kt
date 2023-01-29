package me.cubixor.bettertello.api

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.core.content.res.ResourcesCompat
import me.cubixor.bettertello.R
import me.cubixor.bettertello.utils.Utils.dpToPixels

class SpinnerBackgroundCreator {
    fun createBackground(context: Context, items: Int): Drawable {

        val layers = arrayOfNulls<Drawable>(items)
        val bgDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.button_background, null) as GradientDrawable
        bgDrawable.setSize(dpToPixels(context, 150), dpToPixels(context, items * 42 + 2))

        layers[0] = bgDrawable
        for (i in 1 until items) {
            val gradientDrawable = ShapeDrawable()
            gradientDrawable.paint.color = ResourcesCompat.getColor(context.resources, R.color.light_grey, null)
            layers[i] = gradientDrawable
        }

        val layerDrawable = LayerDrawable(layers)
        for (i in 1 until items) {
            layerDrawable.setLayerSize(i, dpToPixels(context, 150), dpToPixels(context, 2))
            layerDrawable.setLayerInsetTop(i, dpToPixels(context, i * 42))
        }
        return layerDrawable
    }
}