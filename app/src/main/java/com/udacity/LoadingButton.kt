package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

enum class ButtonTextStatus(val label: Int) {
    CLICK_ME(R.string.button_name),
    BUTTON_LOADING(R.string.button_loading);

//    fun nextText()  {
//
//        textStatus = BUTTON_LOADING
//    }
}

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var textStatus = ButtonTextStatus.CLICK_ME
    private var radius = 0.0f                   // Radius of the circle.
    private val pointPosition: PointF = PointF(0.0f, 0.0f)
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {
        isClickable = true

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//// Set dial background color to green if selection not off.
//        paint.color = if (buttonState == ButtonState.Clicked) Color.GRAY else Color.GREEN
//// Draw the dial.
//        if (canvas != null) {
//            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
//        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}