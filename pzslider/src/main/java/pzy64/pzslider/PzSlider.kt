package pzy64.pzslider

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class PzSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var range = Pair(5, 101)
    private var viewWidth = 0
    private var viewHeight = 0
    private var onSeekListener: OnSeekListener? = null

    private var bend = 40f
    private var barStroke = 5f
    private var hemiCircleRadius = 40f
    private var innerCircleRadius = 32f
    private var padding = hemiCircleRadius + bend
    private var touchX = padding
    private var progress = 5000
    private var textHeight = 20f
    private var textSize = 30f
    private var lineColor = "#ffaf49".toColor()
    private var circleColor = "#ff8d00".toColor()
    private var textColor = "#f47100".toColor()


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

    private var textPaint = Paint().apply {
        color = textColor
        textSize = this@PzSlider.textSize
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
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

            drawShape(canvas)

            canvas.restore()
        }
    }


    fun drawShape(canvas: Canvas) {

        val oval = RectF().apply {
            left = touchX - hemiCircleRadius
            right = touchX + hemiCircleRadius
            top = viewHeight / 2 - hemiCircleRadius
            bottom = viewHeight.toFloat() / 2 + hemiCircleRadius + 2
        }


        val bendRight = RectF().apply {
            left = oval.right
            right = oval.right + bend
            bottom = viewHeight / 2f
            top = viewHeight / 2f - bend
        }

        val bendLeft = RectF().apply {
            left = oval.left - bend
            right = oval.left
            bottom = viewHeight / 2f
            top = viewHeight / 2f - bend
        }

        canvas.drawLine(0f, viewHeight / 2f, oval.left - bend / 2, viewHeight / 2f, linePaint)
        canvas.drawLine(oval.right + bend / 2, viewHeight / 2f, viewWidth.toFloat(), viewHeight / 2f, linePaint)

        val path = Path()
        path.addArc(bendLeft, 90f, -bend)
        path.arcTo(oval, 180 + bend, 180 - 2 * bend)
        path.arcTo(bendRight, 90 + bend, -bend)
        canvas.drawPath(path, linePaint)

        canvas.drawCircle(touchX, viewHeight / 2f, innerCircleRadius, paint)

        progress = mapRange(
            Pair(padding.toInt(), (viewWidth - padding).toInt()),
            range,
            touchX.toInt()
        )

        canvas.drawText("₹ ${progress}K", touchX, viewHeight / 2 - hemiCircleRadius - textHeight, textPaint)
    }

    fun setOnSeekListener(listener: OnSeekListener) {
        onSeekListener = listener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (onSeekListener != null) {
                        onSeekListener!!.onProgressStarted(this,progress*1000)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (onSeekListener != null) {
                        onSeekListener!!.onProgressCompleted(this,progress*1000)
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (event.x > padding && event.x < viewWidth - padding)
                        touchX = event.x
                    if (onSeekListener != null) {
                        onSeekListener!!.onProgressChanged(this,progress*1000)
                    }
                    invalidate()

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

    /**
     * Range mapping function
     */

    fun mapRange(domain: Pair<Int, Int>, range: Pair<Int, Int>, value: Int): Int {
        val result =
            range.first + ((value - domain.first) * (range.second - range.first)) /
                    (domain.second - domain.first)
        return result

    }
}