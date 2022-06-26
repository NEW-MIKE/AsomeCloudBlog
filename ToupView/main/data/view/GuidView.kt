package com.example.astroclient.view

import android.R.attr
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class GuidView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var offSet = 10f
    var currentX = -offSet
    var currentY = -offSet
    var paint: Paint = Paint()
    init {

        paint.setColor(Color.GREEN)
        paint.setStyle(Paint.Style.FILL)
        paint.setStrokeWidth(2f)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(
            currentX - offSet,
            currentY - offSet,
            currentX + offSet,
            currentY - offSet,
            paint
        )
        canvas.drawLine(
            currentX - offSet,
            currentY - offSet,
            currentX - offSet,
            currentY + offSet,
            paint
        )
        canvas.drawLine(
            currentX + offSet,
            currentY - offSet,
            currentX + offSet,
            currentY + offSet,
            paint
        )
        canvas.drawLine(
            currentX - offSet,
            currentY + offSet,
            currentX + offSet,
            currentY + offSet,
            paint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        currentX = event.x
        currentY = event.y
        invalidate();
        return true;
    }

}