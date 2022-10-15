package me.cubixor.bettertello.bar;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import me.cubixor.bettertello.MainActivity;
import me.cubixor.bettertello.R;

public enum BarColor {
    OK(R.drawable.bar_ok,
            R.drawable.bar_ok_full,
            R.drawable.bar_ok_expand,
            getColorFromFile(R.color.ok),
            getColorFromFile(R.color.ok_glow)),
    WARNING(R.drawable.bar_warning,
            R.drawable.bar_warning_full,
            R.drawable.bar_warning_expand,
            getColorFromFile(R.color.warning),
            getColorFromFile(R.color.warning_glow)),
    ERROR(R.drawable.bar_error,
            R.drawable.bar_error_full,
            R.drawable.bar_error_expand,
            getColorFromFile(R.color.error),
            getColorFromFile(R.color.error_glow)),
    NEUTRAL(R.drawable.bar_neutral,
            R.drawable.bar_neutral_full,
            R.drawable.bar_neutral_expand,
            getColorFromFile(R.color.neutral),
            getColorFromFile(R.color.neutral_glow));

    private final int mainViewDrawableID;
    private final int homeViewDrawableID;
    private final int expandDrawableID;
    private final int color;
    private final int glowColor;

    BarColor(int mainViewDrawableID, int homeViewDrawableID, int expandDrawableID, int color, int glowColor) {
        this.mainViewDrawableID = mainViewDrawableID;
        this.homeViewDrawableID = homeViewDrawableID;
        this.expandDrawableID = expandDrawableID;
        this.color = color;
        this.glowColor = glowColor;
    }

    private static Drawable getDrawableFromFile(int id) {
        return ContextCompat.getDrawable(MainActivity.getActivity(), id);
    }

    private static int getColorFromFile(int id) {
        return ContextCompat.getColor(MainActivity.getActivity(), id);
    }

    public Drawable getMainViewDrawable() {
        return getDrawableFromFile(mainViewDrawableID);
    }

    public Drawable getHomeViewDrawable() {
        return getDrawableFromFile(homeViewDrawableID);
    }

    public Drawable getExpandDrawable() {
        return getDrawableFromFile(expandDrawableID);
    }

    public int getColor() {
        return color;
    }

    public int getGlowColor() {
        return glowColor;
    }
}
