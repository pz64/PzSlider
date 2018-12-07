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

    private var touchX = 80f
    private var barStroke = 5f
    private var hemiCircleRadius = 50f
    private var innerCircleRadius = 40f
    private var bend = 15f

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
        path.addArc(bendLeft, 90f, -90f)
        path.arcTo(oval, 200f, 140f)
        path.arcTo(bendRight, 180f, -90f)


        canvas.drawPath(path, linePaint)

        canvas.drawCircle(touchX, viewHeight / 2f, innerCircleRadius, paint)
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

}