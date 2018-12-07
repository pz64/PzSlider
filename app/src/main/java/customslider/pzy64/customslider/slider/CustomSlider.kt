package customslider.pzy64.customslider.slider

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class CustomSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var viewWidth = 0
    private var viewHeight = 0
    private var onSeekListener: OnSeekListener? = null

    private var touchX = 0f
    private var barStroke = 5f
    private var hemiCircleRadius = 50f
    private var innerCircleRadius = 40f

    private var lineColor = "#424242".toColor()
    private var circleColor = "#212121".toColor()

    private var linePaint: Paint = Paint().apply {
        color = lineColor
        strokeWidth = barStroke
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    private var paint: Paint = Paint().apply {
        color = circleColor
        isAntiAlias = true
    }

private var backgroundPaint = Paint().apply {
    color = Color.WHITE
    strokeWidth = barStroke + 5
    isAntiAlias = true
}

init {
}

override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    viewWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
    viewHeight = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom

}

override fun onDraw(c: Canvas?) {
    super.onDraw(c)
    c?.let { canvas ->
        canvas.save()

        canvas.drawPaint(backgroundPaint)

        val oval = RectF().apply {
            left = touchX - hemiCircleRadius
            right = touchX + hemiCircleRadius
            top = viewHeight / 2 - hemiCircleRadius
            bottom = viewHeight.toFloat() / 2 + hemiCircleRadius + 2
        }



        canvas.drawArc(oval, 0f, -180f, true, linePaint)

        canvas.drawLine(0f, viewHeight / 2f, oval.left, viewHeight / 2f, linePaint)

        canvas.drawLine(oval.left + 2, viewHeight / 2f + 2, oval.right - 2, viewHeight / 2f + 2, backgroundPaint)

        canvas.drawLine(oval.right, viewHeight / 2f, viewWidth.toFloat(), viewHeight / 2f, linePaint)

        canvas.drawCircle(touchX, viewHeight / 2f, innerCircleRadius, paint)




        canvas.restore()
    }
    invalidate()
}

fun setOnSeekListener(listener: OnSeekListener) {
    onSeekListener = listener
}

override fun onTouchEvent(event: MotionEvent?): Boolean {
    event?.let {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("YYY", "down")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("YYY", "up")
            }

            MotionEvent.ACTION_MOVE -> {
                touchX = event.x
            }
            else -> {
            }

        }
    }
    return true
}

override fun performClick(): Boolean {
    Log.d("YYY", "clicked")
    return super.performClick()

}


/**
 * Color converter
 * */
fun String.toColor() = Color.parseColor(this)

}