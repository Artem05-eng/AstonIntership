package com.example.homework_4

import androidx.core.content.res.ResourcesCompat
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.properties.Delegates

class AnalogTime @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.clockStyle,
    defStyleRes: Int = R.style.AnalogTimerStyle
) : View(context, attrs, defStyleAttr, defStyleRes) {

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
    private var hourColor by Delegates.notNull<Int>()
    private var minuteColor by Delegates.notNull<Int>()
    private var secondColor by Delegates.notNull<Int>()
    private var hourLength by Delegates.notNull<Float>()
    private var minuteLength by Delegates.notNull<Float>()
    private var secondLength by Delegates.notNull<Float>()

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.AnalogTime,
            defStyleAttr,
            defStyleRes
        )
        hourColor = typedArray.getColor(R.styleable.AnalogTime_hourColor, resources.getColor(R.color.red))
        minuteColor = typedArray.getColor(R.styleable.AnalogTime_minuteColor, resources.getColor(R.color.blue))
        secondColor = typedArray.getColor(R.styleable.AnalogTime_secondColor, resources.getColor(R.color.black))
        hourLength = typedArray.getFloat(R.styleable.AnalogTime_hourLength, 0.5f)
        minuteLength = typedArray.getFloat(R.styleable.AnalogTime_minuteLength, 0.65f)
        secondLength = typedArray.getFloat(R.styleable.AnalogTime_secondLength, 0.75f)
    }

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
        paint!!.setColor(secondColor)
        drawHand(canvas, second, secondLength, paint!!)
        //minutes
        paint!!.setColor(minuteColor)
        drawHand(canvas, minute, minuteLength, paint!!)
        //hours
        paint!!.setColor(hourColor)
        drawHand(canvas, (hour + minute / 60) * 5f, hourLength, paint!!)
    }

    private fun drawHand(canvas: Canvas, loc: Double, handRadius: Float, paint: Paint) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        canvas.drawLine(
            (width / 2 - Math.cos(angle) * 50).toFloat(),
            (height / 2 - Math.sin(angle) * 50).toFloat(),
            (width / 2 + Math.cos(angle) * handRadius * radius).toFloat(),
            (height / 2 + Math.sin(angle) * handRadius * radius).toFloat(),
            paint!!
        )
    }
}