package me.cubixor.bettertello.utils;

import static me.cubixor.bettertello.utils.Utils.getStr;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.widget.TextView;

import me.cubixor.bettertello.R;
import me.cubixor.bettertello.bar.BarState;

public class BarAnimationUtils {

    public static void changeText(Context context, BarState to, TextView textView) {
        Animation out = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_out_text);
        Animation in = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_text);

        textView.startAnimation(out);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            textView.setText(getStr(to.getTextPath()));
            textView.startAnimation(in);
        }, 300);
    }
}
