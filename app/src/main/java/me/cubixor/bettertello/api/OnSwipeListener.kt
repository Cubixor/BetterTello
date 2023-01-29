package me.cubixor.bettertello.api;

import android.view.GestureDetector;
import android.view.MotionEvent;

import me.cubixor.telloapi.api.FlipDirection;

public abstract class OnSwipeListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float x1 = e1.getX();
        float y1 = e1.getY();

        float x2 = e2.getX();
        float y2 = e2.getY();

        FlipDirection direction = getDirection(x1, y1, x2, y2);
        return onSwipe(direction);
    }

    public abstract boolean onSwipe(FlipDirection direction);

    private FlipDirection getDirection(float x1, float y1, float x2, float y2) {
        double angle = getAngle(x1, y1, x2, y2);
        return fromAngle(angle);
    }

    private double getAngle(float x1, float y1, float x2, float y2) {
        double rad = Math.atan2(y1 - y2, x2 - x1) + Math.PI;
        return (rad * 180 / Math.PI + 180) % 360;
    }

    private FlipDirection fromAngle(double angle) {
        if (inRange(angle, 0, 22.5f) || inRange(angle, 337.5f, 360)) {
            return FlipDirection.RIGHT;
        } else if (inRange(angle, 22.5f, 67.5f)) {
            return FlipDirection.FORWARD_RIGHT;
        } else if (inRange(angle, 67.5f, 112.5f)) {
            return FlipDirection.FORWARD;
        } else if (inRange(angle, 112.5f, 157.5f)) {
            return FlipDirection.FORWARD_LEFT;
        } else if (inRange(angle, 157.5f, 202.5f)) {
            return FlipDirection.LEFT;
        } else if (inRange(angle, 202.5f, 247.5f)) {
            return FlipDirection.BACKWARD_LEFT;
        } else if (inRange(angle, 247.5f, 292.5f)) {
            return FlipDirection.BACKWARD;
        } else if (inRange(angle, 292.5f, 337.5f)) {
            return FlipDirection.BACKWARD_RIGHT;
        }
        return null;
    }

    private boolean inRange(double angle, float init, float end) {
        return (angle >= init) && (angle < end);
    }
}