package pzy64.pzslider

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class PzSlider @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var range: Pair<Int, Int>
    private var viewWidth = 0
    private var viewHeight = 0
    private var onSeekListener: OnSeekListener? = null

    private var bend: Float
    private var barStroke: Float
    private var semiCircleRadius: Float
    private var innerCircleRadius: Float
    private var padding: Float
    private var touchX: Float
    private var knobPosition: Float
    private var progress: Int
    private var textHeight: Float
    private var textSize: Float
    private var lineColor: Int
    private var circleColor: Int
    private var textColor: Int
    private var knobAccelerationFactor: Float

    private var linePaint: Paint
    private var paint: Paint
    private var backgroundPaint = Paint()
    private var textPaint = Paint()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PzSlider)
        bend = typedArray.getFloat(R.styleable.PzSlider_bend, 40f)
        barStroke = typedArray.getFloat(R.styleable.PzSlider_barStrokeAmount, 2f)
        semiCircleRadius = typedArray.getFloat(R.styleable.PzSlider_semiCircleRadius, 40f)
        innerCircleRadius = typedArray.getFloat(R.styleable.PzSlider_innerCircleRadius, 32f)
        textHeight = typedArray.getFloat(R.styleable.PzSlider_textHeight, 20f)
        textSize = typedArray.getFloat(R.styleable.PzSlider_textSize, 30f)
        textColor = typedArray.getColor(R.styleable.PzSlider_textColor, "#f47100".toColor())
        circleColor = typedArray.getColor(R.styleable.PzSlider_circleColor, "#ff8d00".toColor())
        lineColor = typedArray.getColor(R.styleable.PzSlider_lineColor, "#ffaf49".toColor())
        knobAccelerationFactor = typedArray.getFloat(R.styleable.PzSlider_knobAccelerationFactor, 3f)

        range = Pair(
            typedArray.getInteger(R.styleable.PzSlider_rangeStart, 5000) / 1000,
            typedArray.getInteger(R.styleable.PzSlider_rangeEnd, 101000) / 1000
        )
        typedArray.recycle()

        padding = semiCircleRadius + bend
        touchX = padding
        progress = 5000

        knobPosition = touchX

        linePaint = Paint().apply {
            color = lineColor
            strokeWidth = barStroke
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        paint = Paint().apply {
            color = circleColor
            isAntiAlias = true
        }
        backgroundPaint = Paint().apply {
            color = Color.WHITE
            strokeWidth = barStroke + 5
            isAntiAlias = true
        }
        textPaint = Paint().apply {
            color = textColor
            textSize = this@PzSlider.textSize
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

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

            animateKnob()

            canvas.restore()
        }
    }

    private fun animateKnob() {
        if (touchX != knobPosition) {
            if (touchX > knobPosition) {
                val acceleration = (touchX - knobPosition) / knobAccelerationFactor
                knobPosition += if (acceleration < 1) 1f else acceleration
            }
           else if (touchX < knobPosition) {
                val acceleration = (knobPosition - touchX) / knobAccelerationFactor
                knobPosition -= if (acceleration < 1) 1f else acceleration
            }
            if (abs((touchX - knobPosition)) < 1f)
                knobPosition = touchX
            invalidate()
        } else {
            if (onSeekListener != null) {
                onSeekListener!!.onProgressCompleted(this, progress * 1000)
            }
        }
    }

    private fun drawShape(canvas: Canvas) {

        val oval = RectF().apply {
            left = knobPosition - semiCircleRadius
            right = knobPosition + semiCircleRadius
            top = viewHeight / 2 - semiCircleRadius
            bottom = viewHeight.toFloat() / 2 + semiCircleRadius + 2
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

        canvas.drawCircle(knobPosition, viewHeight / 2f, innerCircleRadius, paint)

        progress = mapRange(
            Pair(padding.toInt(), (viewWidth - padding).toInt()),
            range,
            knobPosition.toInt()
        )

        canvas.drawText("₹ ${progress}K", knobPosition, viewHeight / 2 - semiCircleRadius - textHeight, textPaint)

        if (onSeekListener != null) {
            onSeekListener!!.onProgressChanged(this, progress * 1000)
        }
    }

    fun setOnSeekListener(listener: OnSeekListener) {
        onSeekListener = listener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (onSeekListener != null) {
                        onSeekListener!!.onProgressStarted(this, progress * 1000)
                    }
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    invalidate()
                }

                MotionEvent.ACTION_MOVE -> {
                    if (event.x > padding && event.x < viewWidth - padding)
                        touchX = event.x
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