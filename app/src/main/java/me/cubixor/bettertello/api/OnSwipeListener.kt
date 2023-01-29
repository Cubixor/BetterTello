package me.cubixor.bettertello.api

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import me.cubixor.telloapi.api.FlipDirection
import kotlin.math.atan2

abstract class OnSwipeListener : SimpleOnGestureListener() {
    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val x1 = e1.x
        val y1 = e1.y
        val x2 = e2.x
        val y2 = e2.y
        val direction = getDirection(x1, y1, x2, y2)
        return onSwipe(direction)
    }

    abstract fun onSwipe(direction: FlipDirection): Boolean
    private fun getDirection(x1: Float, y1: Float, x2: Float, y2: Float): FlipDirection {
        val angle = getAngle(x1, y1, x2, y2)
        return fromAngle(angle)
    }

    private fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Double {
        val rad = atan2((y1 - y2).toDouble(), (x2 - x1).toDouble()) + Math.PI
        return (rad * 180 / Math.PI + 180) % 360
    }

    private fun fromAngle(angle: Double): FlipDirection {
        /*if (inRange(angle, 0f, 22.5f) || inRange(angle, 337.5f, 360f)) {
            return FlipDirection.RIGHT
        } else*/
        return if (inRange(angle, 22.5f, 67.5f)) FlipDirection.FORWARD_RIGHT
        else if (inRange(angle, 67.5f, 112.5f)) FlipDirection.FORWARD
        else if (inRange(angle, 112.5f, 157.5f)) FlipDirection.FORWARD_LEFT
        else if (inRange(angle, 157.5f, 202.5f)) FlipDirection.LEFT
        else if (inRange(angle, 202.5f, 247.5f)) FlipDirection.BACKWARD_LEFT
        else if (inRange(angle, 247.5f, 292.5f)) FlipDirection.BACKWARD
        else if (inRange(angle, 292.5f, 337.5f)) FlipDirection.BACKWARD_RIGHT
        else FlipDirection.RIGHT

        //return null
    }

    private fun inRange(angle: Double, init: Float, end: Float): Boolean {
        return angle >= init && angle < end
    }
}