package me.cubixor.bettertello.api;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;

import androidx.core.content.res.ResourcesCompat;

import me.cubixor.bettertello.R;
import me.cubixor.bettertello.utils.Utils;

public class SpinnerBackgroundCreator {

    public Drawable createBackground(Context context, int items) {

        Drawable[] layers = new Drawable[items];

        GradientDrawable bgDrawable = (GradientDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_background, null);
        bgDrawable.setSize(Utils.dpToPixels(context, 150), Utils.dpToPixels(context, items * 42 + 2));
        layers[0] = bgDrawable;


        for (int i = 1; i < items; i++) {
            ShapeDrawable gradientDrawable = new ShapeDrawable();
            gradientDrawable.getPaint().setColor(ResourcesCompat.getColor(context.getResources(), R.color.light_grey, null));

            layers[i] = gradientDrawable;
        }

        LayerDrawable layerDrawable = new LayerDrawable(layers);

        for (int i = 1; i < items; i++) {
            layerDrawable.setLayerSize(i, Utils.dpToPixels(context, 150), Utils.dpToPixels(context, 2));
            layerDrawable.setLayerInsetTop(i, Utils.dpToPixels(context, i * 42));
        }

        return layerDrawable;

    }
}
