package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var backGroundColor: Int = Color.BLACK
    private var textColor: Int = Color.BLACK // default color

    // tells the compiler that the value of a variable
    // must never be cached as its value may change outside
    @Volatile
    private var progress: Double = 0.0

    private val pointPosition: PointF = PointF(0.0f, 0.0f)
    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }
    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        invalidate()    // redraw the screen
        requestLayout() // when rectangular progress dimension changes
    }
    // call after downloading is completed
    fun hasCompletedDownload() {
        // cancel the animation when file is downloaded
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        //Return button bacground color to normal state
        ContextCompat.getColor(context, R.color.colorPrimary)

        invalidate()
        requestLayout()
    }

    // call after downloading is not completed
    fun hasNotCompletedDownload() {
        // cancel the animation when file is downloaded
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        //Return button bacground color to normal state
        ContextCompat.getColor(context, R.color.colorPrimary)

        invalidate()
        requestLayout()
    }


    init {
        isClickable = true
        valueAnimator = AnimatorInflater.loadAnimator(
            context,
            // properties for downloading progress is defined
            R.animator.loading_animation
        ) as ValueAnimator

        valueAnimator.addUpdateListener(updateListener)

        // initialize custom attributes of the button
        val attr = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0,
            0
        )

        try {

            // button back-ground color
            backGroundColor = attr.getColor(
                R.styleable.LoadingButton_backgroundColor,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )

            // button text color
            textColor = attr.getColor(
                R.styleable.LoadingButton_textColor,
                ContextCompat.getColor(context, R.color.white)
            )
        } finally {
            // clearing all the data associated with attribute
            attr.recycle()
        }

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (buttonState == ButtonState.Completed) buttonState = ButtonState.Loading
        animation()
        return true
    }
    // start the animation when button is clicked
    private fun animation() {
        valueAnimator.start()
    }
    fun reset()
    {
        ButtonState.Completed
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.strokeWidth = 0f
        paint.color = backGroundColor
        // draw custom button
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // to show rectangular progress on custom button while file is downloading
        if (buttonState == ButtonState.Loading) {
            paint.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            canvas?.drawRect(
                0f, 0f,
                (width * (progress / 100)).toFloat(), height.toFloat(), paint
            )
        }
        // check the button state
        val buttonText = if (buttonState == ButtonState.Loading)
            resources.getString(R.string.button_loading)  // We are loading as button text
        else resources.getString(R.string.button_name)// download as button text

        // write the text on custom button
        paint.color = textColor
        canvas?.drawText(buttonText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            View.MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}