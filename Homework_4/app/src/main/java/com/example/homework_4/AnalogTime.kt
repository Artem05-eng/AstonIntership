package com.example.homework_4

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import java.util.*

class AnalogTime @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mHeight = 0
    private var mWidth = 0
    private var padding = 0
    private val numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation: Int = 0
    private var radius = 0
    private var paint: Paint? = null
    private var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    private fun initClock() {
        mHeight = getHeight()
        mWidth = getWidth()
        padding = numeralSpacing + 50
        val min = Math.min(height, width)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        paint = Paint()
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }
        canvas.drawColor(Color.WHITE)
        drawCircle(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
        postInvalidateDelayed(1000)
    }

    //отрисовка круга
    private fun drawCircle(canvas: Canvas) {
        paint!!.reset()
        paint!!.color = resources.getColor(R.color.black)
        paint!!.strokeWidth = 20f
        paint!!.style = Paint.Style.STROKE
        paint!!.isAntiAlias = true
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            radius + padding - 10.toFloat(),
            paint!!
        )
    }

    //отрисовка засечек
    private fun drawNumeral(canvas: Canvas) {
        val path = Path()
        for (number in numbers) {
            val angle = Math.PI / 6 * (number - 3)
            val x1 = (width / 2 + Math.cos(angle) * (radius + padding - 20.toFloat())).toFloat()
            val y1 = (height / 2 + Math.sin(angle) * (radius + padding - 20.toFloat())).toFloat()
            val x2 = (width / 2 + Math.cos(angle) * radius).toFloat()
            val y2 = (height / 2 + Math.sin(angle) * radius).toFloat()
            path.reset()
            path.moveTo(x1, y1)
            path.lineTo(x2, y2)
            canvas.drawPath(path, paint!!)
        }
    }

    private fun drawHands(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        val mHour = calendar.get(Calendar.HOUR)
        val hour = if (mHour > 12) mHour - 12 else mHour
        val minute = calendar.get(Calendar.MINUTE).toDouble()
        val second = calendar.get(Calendar.SECOND).toDouble()
        //seconds
        paint!!.setColor(Color.BLACK)
        drawHand(canvas, second, radius-100, paint!!)
        //minutes
        paint!!.setColor(Color.RED)
        drawHand(canvas, minute, radius-150, paint!!)
        //hours
        paint!!.setColor(Color.BLUE)
        drawHand(canvas, (hour + minute/60) * 5f, radius-200, paint!!)
    }

    private fun drawHand(canvas: Canvas, loc: Double, handRadius: Int, paint: Paint) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        canvas.drawLine((width / 2).toFloat(), (height / 2).toFloat(),
            (width / 2 + Math.cos(angle) * handRadius).toFloat(),
            (height / 2 + Math.sin(angle) * handRadius).toFloat(),
            paint!!)
        canvas.drawLine((width / 2).toFloat(), (height / 2).toFloat(),
            (width / 2 - Math.cos(angle) * 50).toFloat(),
            (height / 2 - Math.sin(angle) * 50).toFloat(),
            paint!!)
    }
}