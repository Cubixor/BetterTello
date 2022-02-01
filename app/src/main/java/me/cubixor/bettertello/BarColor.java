package me.cubixor.bettertello;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public enum BarColor {
    OK(getDrawableFromFile(R.drawable.bar_ok)),
    WARNING(getDrawableFromFile(R.drawable.bar_warning)),
    ERROR(getDrawableFromFile(R.drawable.bar_error)),
    NEUTRAL(getDrawableFromFile(R.drawable.bar_neutral));

    private final Drawable drawable;

    BarColor(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    private static Drawable getDrawableFromFile(int id) {
        return ContextCompat.getDrawable(MainActivity.getInstance(), id);
    }
}
