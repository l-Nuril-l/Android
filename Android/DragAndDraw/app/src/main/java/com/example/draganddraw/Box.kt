package com.example.draganddraw

import android.graphics.PointF

class Box(val start: PointF) {
    var end: PointF = start
    var rotateStart: PointF? = null
    var rotateIndex: Int? = null
    var rotate: Float = 0F;
    val left: Float
        get() = Math.min(start.x, end.x)
    val right: Float
        get() = Math.max(start.x, end.x)
    val top: Float
        get() = Math.min(start.y, end.y)
    val bottom: Float
        get() = Math.max(start.y, end.y)

    val centerX: Float
        get() = right - left
    val centerY: Float
        get() = bottom - top
}