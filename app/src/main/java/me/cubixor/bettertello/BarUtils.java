package me.cubixor.bettertello;

import static me.cubixor.bettertello.Utils.getStr;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class BarUtils {

    private BarState currentBar;
    private final TextView stateText;
    private final LinkedList<BarState> activeStates = new LinkedList<>();

    public BarUtils() {
        activeStates.add(BarState.NOT_CONNECTED);
        currentBar = BarState.NOT_CONNECTED;
        stateText = MainActivity.getInstance().findViewById(R.id.stateText);
    }

    public BarState getCurrentBar() {
        return currentBar;
    }

    public void addState(BarState toPut) {
        System.out.println("ADD1" + activeStates);
        for (BarState barState : new LinkedList<>(activeStates)) {
            if (barState.getPriority() <= toPut.getPriority()) {
                activeStates.add(activeStates.indexOf(barState), toPut);
                break;
            }
        }
        if (toPut.getPriority() >= currentBar.getPriority()) {
            changeBar(toPut);
        }
        System.out.println("ADD2" + activeStates);

    }

    public void removeState(BarState toRemove) {
        System.out.println("REMOVE1" + activeStates);

        int index = activeStates.indexOf(toRemove);
        activeStates.remove(toRemove);

        if (currentBar.equals(toRemove)) {
            BarState nextBar;
            if (activeStates.size() == 0) {
                nextBar = BarState.LOST_CONNECTION;
            } else if (activeStates.size() > index) {
                nextBar = activeStates.get(index);
            } else {
                nextBar = activeStates.get(index - 1);
            }
            changeBar(nextBar);
        }
        System.out.println("REMOVE2" + activeStates);
    }

    private void changeBar(BarState barState) {
        String text = getStr(barState.getTextPath());

        changeBarColor(barState);
        changeText(barState);
    }

    private void changeBarColor(BarState to) {
        Drawable d1 = currentBar.getBarColor().getDrawable();
        Drawable d2 = to.getBarColor().getDrawable();
        Drawable[] dw = {d1, d2};
        TransitionDrawable transitionDrawable = new TransitionDrawable(dw);
        ImageView bar = MainActivity.getInstance().findViewById(R.id.topBackground);

        bar.setImageDrawable(transitionDrawable);
        transitionDrawable.setCrossFadeEnabled(true);

        int durationMillis = 300;
        transitionDrawable.startTransition(durationMillis);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            currentBar = to;
        }, durationMillis);
    }

    public void changeText(BarState to) {
        Animation out = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.slide_out);
        Animation in = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.slide_in);
        stateText.startAnimation(out);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            stateText.setText(getStr(to.getTextPath()));
            stateText.startAnimation(in);
        }, 300);
    }

    public LinkedList<BarState> getActiveStates() {
        return activeStates;
    }
}
