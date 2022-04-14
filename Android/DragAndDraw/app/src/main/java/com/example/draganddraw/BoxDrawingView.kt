package com.example.draganddraw

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val TAG = "BoxDrawingView"

class BoxDrawingView(context: Context, attrs: AttributeSet? = null): View(context,attrs) {
    private var currentBox: Box? = null
    private val boxes = mutableListOf<Box>()
    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val current = PointF(event!!.x, event.y)

        var action = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                currentBox = Box(current).also {
                    boxes.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                updateCurrentBox(current)
                Log.i("MOOVEE", "INDEX = ${event.actionIndex}, PC = ${event.pointerCount}")
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentBox(current)
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }

        when(event.actionMasked) {
            5 -> {
                Log.i("DOWN", "DOWN")
                currentBox?.let{
                    it.rotateStart = current
                    it.rotateIndex = event.actionIndex
                }
                invalidate()
            }
            6 -> {
                Log.i("UP", "UP")
                currentBox?.rotateIndex?.let {
                    rotateCurrentBox(PointF(event.getX(currentBox?.rotateIndex!!),event.getY(currentBox?.rotateIndex!!)))
                }
                currentBox?.let{
                    it.rotateStart = null
                    it.rotateIndex = null
                }
                invalidate()
            }

        }

        if(event.actionIndex == 0) return true
        Log.i("TEST_ACTION", "ActionMasked ${event.actionMasked} ActionIndex ${event.actionIndex} PointId0 ${event.getPointerId(0)}" )

        //Log.i("SECOND TOUCH", "x = ${event.getX(1)}, y = ${event.getY(1)}" )
        Log.i(TAG, "$action at x = ${current.x}, y = ${current.y}")
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawPaint(backgroundPaint)
            boxes.forEach { box ->
                save();
                rotate(box.rotate);
                drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
                restore();
            }
        }
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }

    private fun rotateCurrentBox(current: PointF) {
        currentBox?.let {
            it.rotate = (it.rotate + (current.x - it.rotateStart!!.x) + (current.y - it.rotateStart!!.y)) % 360
            Log.i("ROTATE", "rotate = ${it.rotate}" )
            invalidate()
        }
    }

}