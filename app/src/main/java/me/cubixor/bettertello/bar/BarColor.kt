package me.cubixor.bettertello.bar

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import me.cubixor.bettertello.R

enum class BarColor(
    private val mainViewDrawableID: Int,
    private val homeViewDrawableID: Int,
    private val expandDrawableID: Int,
    private val color: Int,
    private val glowColor: Int
) {
    OK(
        R.drawable.bar_ok,
        R.drawable.bar_ok_full,
        R.drawable.bar_ok_expand,
        R.color.ok,
        R.color.ok_glow
    ),
    WARNING(
        R.drawable.bar_warning,
        R.drawable.bar_warning_full,
        R.drawable.bar_warning_expand,
        R.color.warning,
        R.color.warning_glow
    ),
    ERROR(
        R.drawable.bar_error,
        R.drawable.bar_error_full,
        R.drawable.bar_error_expand,
        R.color.error,
        R.color.error_glow
    ),
    NEUTRAL(
        R.drawable.bar_neutral,
        R.drawable.bar_neutral_full,
        R.drawable.bar_neutral_expand,
        R.color.neutral,
        R.color.neutral_glow
    );

    private fun getDrawableFromFile(context: Context, id: Int): Drawable? =
        ContextCompat.getDrawable(context, id)


    private fun getColorFromFile(context: Context, id: Int): Int =
        ContextCompat.getColor(context, id)


    fun getMainViewDrawable(context: Context): Drawable? =
        getDrawableFromFile(context, mainViewDrawableID)


    fun getHomeViewDrawable(context: Context): Drawable? =
        getDrawableFromFile(context, homeViewDrawableID)


    fun getExpandDrawable(context: Context): Drawable? =
        getDrawableFromFile(context, expandDrawableID)


    fun getColor(context: Context): Int = getColorFromFile(context, color)


    fun getGlowColor(context: Context): Int = getColorFromFile(context, glowColor)

}