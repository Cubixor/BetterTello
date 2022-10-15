package me.cubixor.bettertello.bar;

import static me.cubixor.bettertello.utils.Utils.getStr;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.sanojpunchihewa.glowbutton.GlowButton;

import java.util.LinkedList;

import me.cubixor.bettertello.Activities;
import me.cubixor.bettertello.HomePageActivity;
import me.cubixor.bettertello.MainActivity;
import me.cubixor.bettertello.R;
import me.cubixor.bettertello.utils.Utils;

public class BarStateManager {

    private static BarStateManager instance;
    private final LinkedList<BarState> activeStates = new LinkedList<>();
    private BarState currentBar;

    public BarStateManager() {
        instance = this;
        activeStates.add(BarState.NOT_CONNECTED);
        currentBar = BarState.NOT_CONNECTED;
    }

    public static BarStateManager getInstance() {
        return instance;
    }

    public BarState getCurrentBar() {
        return currentBar;
    }

    public LinkedList<BarState> getActiveStates() {
        return activeStates;
    }

    public void addRemoveState(BarState state, boolean add) {
        int statesBefore = getActiveStates().size();

        if (getActiveStates().contains(state)) {
            if (!add) {
                removeState(state);
            }
        } else if (add) {
            addState(state);
        }

        int statesAfter = getActiveStates().size();
        if (statesBefore <= 1 && statesAfter > 1) {
            showExpandButton();
        } else if (statesBefore > 1 && statesAfter <= 1) {
            hideExpandButton();
        }
    }

    public void addState(BarState toPut) {
        if (activeStates.contains(toPut)) {
            return;
        }

        for (BarState barState : new LinkedList<>(activeStates)) {
            if (barState.getPriority() <= toPut.getPriority()) {
                activeStates.add(activeStates.indexOf(barState), toPut);
                break;
            }
        }

        addToExpanded(toPut);

        if (toPut.getPriority() >= currentBar.getPriority()) {
            changeBar(toPut);
        }

    }

    public void removeState(BarState toRemove) {
        if (!activeStates.contains(toRemove)) {
            return;
        }

        int index = activeStates.indexOf(toRemove);

        removeFromExpanded(toRemove);
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
    }

    private void changeBar(BarState barState) {
        changeBarMainActivity(barState);
        changeBarHomeActivity(barState);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> currentBar = barState, 300);
    }

    private void changeBarMainActivity(BarState barState) {
        Drawable d1 = currentBar.getBarColor().getMainViewDrawable();
        Drawable d2 = barState.getBarColor().getMainViewDrawable();

        Drawable[] dw = {d1, d2};

        TransitionDrawable transitionDrawable = new TransitionDrawable(dw);
        ImageView bar = MainActivity.getActivity().findViewById(R.id.topBackground);
        bar.setImageDrawable(transitionDrawable);
        transitionDrawable.setCrossFadeEnabled(true);
        transitionDrawable.startTransition(600);


        TextView stateText = MainActivity.getActivity().findViewById(R.id.stateText);
        changeText(barState, stateText);
    }

    private void changeBarHomeActivity(BarState barState) {
        HomePageActivity activity = Activities.getHomePage();

        if (activity == null) {
            return;
        }


        activity.runOnUiThread(() -> {
            Drawable previousBarDrawable = currentBar.getBarColor().getHomeViewDrawable();
            Drawable newBarDrawable = barState.getBarColor().getHomeViewDrawable();
            Drawable[] barDrawables = {previousBarDrawable, newBarDrawable};

            Drawable previousExpandDrawable = currentBar.getBarColor().getExpandDrawable();
            Drawable newExpandDrawable = barState.getBarColor().getExpandDrawable();
            Drawable[] expandDrawables = {previousExpandDrawable, newExpandDrawable};

            TransitionDrawable barTransition = new TransitionDrawable(barDrawables);
            barTransition.setCrossFadeEnabled(true);

            TransitionDrawable expandTransition = new TransitionDrawable(expandDrawables);
            expandTransition.setCrossFadeEnabled(true);

            //int previousGlowColor = currentBar.getBarColor().getGlowColor();
            int newGlowColor = barState.getBarColor().getGlowColor();

            GlowButton stateBar = activity.findViewById(R.id.homeStateBar);
            stateBar.setForeground(barTransition);

            GlowButton expandButton = activity.findViewById(R.id.expandStatesButton);
            expandButton.setForeground(expandTransition);

            barTransition.startTransition(600);
            expandTransition.startTransition(600);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                stateBar.setGlowColor(newGlowColor);
                expandButton.setGlowColor(newGlowColor);
            }, 300);

            TextView stateText = activity.findViewById(R.id.stateText2);
            changeText(barState, stateText);
        });
    }


    private void changeText(BarState to, TextView textView) {
        Animation out = AnimationUtils.loadAnimation(MainActivity.getActivity(), R.anim.slide_out_text);
        Animation in = AnimationUtils.loadAnimation(MainActivity.getActivity(), R.anim.slide_in_text);

        textView.startAnimation(out);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            textView.setText(getStr(to.getTextPath()));
            textView.startAnimation(in);
        }, 300);
    }

    public void setupHomeActivity(HomePageActivity activity) {
        TextView stateText = activity.findViewById(R.id.stateText2);
        stateText.setText(getStr(currentBar.getTextPath()));

        GlowButton stateBar = activity.findViewById(R.id.homeStateBar);
        stateBar.setForeground(currentBar.getBarColor().getHomeViewDrawable());
        stateBar.setGlowColor(currentBar.getBarColor().getGlowColor());

        GlowButton expandButton = activity.findViewById(R.id.expandStatesButton);
        expandButton.setForeground(currentBar.getBarColor().getExpandDrawable());
        expandButton.setGlowColor(currentBar.getBarColor().getGlowColor());

        if (getActiveStates().size() <= 1) {
            expandButton.setVisibility(View.INVISIBLE);
        }
    }

    public void showExpandButton() {
        HomePageActivity activity = Activities.getHomePage();

        if (activity == null) {
            return;
        }

        activity.runOnUiThread(() -> {
            GlowButton expandButton = activity.findViewById(R.id.expandStatesButton);
            Animation in = AnimationUtils.loadAnimation(MainActivity.getActivity(), R.anim.slide_in_expand);

            expandButton.setVisibility(View.VISIBLE);

            in.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    expandButton.setElevation(11);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            expandButton.startAnimation(in);
        });
    }

    public void hideExpandButton() {
        HomePageActivity activity = Activities.getHomePage();

        if (activity == null) {
            return;
        }

        activity.runOnUiThread(() -> {
            GlowButton expandButton = activity.findViewById(R.id.expandStatesButton);
            Animation out = AnimationUtils.loadAnimation(MainActivity.getActivity(), R.anim.slide_out_expand);

            expandButton.setElevation(0);
            if (activity.areStatesExpanded()) {
                expandButton.animate().rotationBy(180).setDuration(300);
                activity.setStatesExpanded(false);
            }

            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    expandButton.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            expandButton.startAnimation(out);
        });
    }

    public void expandStates(HomePageActivity activity, GlowButton view) {
        view.animate().rotationBy(-180).setDuration(300);

        for (BarState barState : getActiveStates()) {
            int index = getActiveStates().indexOf(barState) + 1;

            if (index == 1) {
                continue;
            }

            createStateBar(activity, barState, index - 1);
        }

        activity.setStatesExpanded(true);
    }

    public void createStateBar(HomePageActivity activity, BarState barState, int index) {
        ConstraintLayout constraintLayout = activity.findViewById(R.id.home_page);
        int id = View.generateViewId();

        TextView textView = new TextView(activity);
        textView.setId(id);
        textView.setWidth(Utils.dpToPixels(activity, 440));
        textView.setHeight(Utils.dpToPixels(activity, 40));
        textView.setText(barState.getTextPath());
        textView.setTextColor(activity.getColor(R.color.white));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setBackground(barState.getBarColor().getHomeViewDrawable());
        textView.setPaddingRelative(Utils.dpToPixels(activity, 20), 0, 0, 0);
        textView.setOutlineProvider(null);
        textView.setElevation(10 - index);
        constraintLayout.addView(textView, index);


        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(id, ConstraintSet.BOTTOM, R.id.homeStateLayout, ConstraintSet.BOTTOM);
        constraintSet.connect(id, ConstraintSet.TOP, R.id.homeStateLayout, ConstraintSet.TOP);
        constraintSet.connect(id, ConstraintSet.END, R.id.homeStateLayout, ConstraintSet.END);
        constraintSet.connect(id, ConstraintSet.START, R.id.homeStateLayout, ConstraintSet.START);
        constraintSet.applyTo(constraintLayout);

        activity.getStateBars().add(index - 1, textView);

        Animation in = getAnimation(0, -Utils.dpToPixels(activity, (index) * 50));

        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.bringToFront();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        textView.startAnimation(in);
    }

    public void shrinkStates(HomePageActivity activity, GlowButton view) {
        view.animate().rotationBy(180).setDuration(300);

        activity.findViewById(R.id.homeStateLayout).bringToFront();

        for (TextView textView : activity.getStateBars()) {
            slideOutState(activity, textView);
        }

        activity.getStateBars().clear();

        activity.setStatesExpanded(false);
    }

    private void slideOutState(HomePageActivity activity, TextView textView) {
        Animation out = getAnimation(-Utils.dpToPixels(activity, (activity.getStateBars().indexOf(textView) + 1) * 50), 0);

        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((ViewGroup) textView.getParent()).removeView(textView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        textView.startAnimation(out);
    }

    private Animation getAnimation(int fromY, int toY) {
        Animation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                fromY == 0 ? Animation.RELATIVE_TO_SELF : Animation.ABSOLUTE, fromY,
                toY == 0 ? Animation.RELATIVE_TO_SELF : Animation.ABSOLUTE, toY);
        anim.setDuration(300);
        anim.setFillAfter(true);

        return anim;
    }

    public void removeFromExpanded(BarState barState) {
        HomePageActivity activity = Activities.getHomePage();

        if (activity == null) {
            return;
        }

        if (!activity.areStatesExpanded()) {
            return;
        }

        activity.runOnUiThread(() -> {
            int index = getActiveStates().indexOf(barState) - 1;

            if (index < 0) {
                index = 0;
            }

            TextView textToRemove = activity.getStateBars().get(index);

            slideOutState(activity, textToRemove);
            activity.getStateBars().remove(textToRemove);

            for (TextView textView : activity.getStateBars()) {
                if (activity.getStateBars().indexOf(textView) < index) {
                    continue;
                }

                Animation slide = getAnimation(
                        -Utils.dpToPixels(activity, (activity.getStateBars().indexOf(textView) + 2) * 50),
                        -Utils.dpToPixels(activity, (activity.getStateBars().indexOf(textView) + 1) * 50));

                textView.startAnimation(slide);
            }
        });
    }

    public void addToExpanded(BarState barState) {
        HomePageActivity activity = Activities.getHomePage();

        if (activity == null) {
            return;
        }

        if (!activity.areStatesExpanded()) {
            return;
        }

        activity.runOnUiThread(() -> {
            BarState barState1 = barState;

            int index = getActiveStates().indexOf(barState1) - 1;
            if (index < 0) {
                if (getActiveStates().size() < 2) {
                    return;
                }

                index = 0;
                barState1 = getActiveStates().get(1);
            }

            createStateBar(activity, barState1, index + 1);


            for (TextView textView : activity.getStateBars()) {
                if (activity.getStateBars().indexOf(textView) <= index) {
                    continue;
                }

                Animation slide = getAnimation(
                        -Utils.dpToPixels(activity, (activity.getStateBars().indexOf(textView)) * 50),
                        -Utils.dpToPixels(activity, (activity.getStateBars().indexOf(textView) + 1) * 50));

                textView.startAnimation(slide);
            }
        });
    }
}
