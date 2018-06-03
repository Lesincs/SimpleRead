package nov.lesincs.twogoods.cusotom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.lesincs.simpleread.R
import com.lesincs.simpleread.ui.activity.toast


/**
 * Created by cs丶 on 2018/5/7 18:36.
　文件描述:自定义的圆形指示器
 */
class CirclePointIndicator : View, ViewPager.OnPageChangeListener {

    private var mIndicatorCount: Int = 0
    private var mCurrentSelectedIndex = 0

    private var mSelectedCirclePointColor = 0
    private var mUnselectedCirclePointColor = 0
    private var mViewPagerOffset = 0f

    private var mIndicatorRadius = 0f
    private var mIndicatorSpace = 0f

    private lateinit var mSelectedCirclePointPaint: Paint
    private lateinit var mUnselectedCirclePointPaint: Paint

    private val DEFAULT_SELECTED_CIRCLE_POINT_COLOR = Color.DKGRAY
    private val DEFAULT_UNSELECTED_CIRCLE_POINT_COLOR = Color.RED
    private val DEFAULT_INDICATOR_RADIUS = 10f
    private val DEFAULT_INDICATOR_SPACE = 5f

    private var isBindToViewPager = false

    constructor(context: Context) : super(context)

    @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CirclePointIndicator)

        mIndicatorCount = ta.getInt(R.styleable.CirclePointIndicator_indicatorCount, 5)

        val radiusDp = ta.getDimension(R.styleable.CirclePointIndicator_circlePointRadius, DEFAULT_INDICATOR_RADIUS)
        mIndicatorRadius = dp2px(context, radiusDp)

        val spaceDp = ta.getDimension(R.styleable.CirclePointIndicator_indicatorSpace, DEFAULT_INDICATOR_SPACE)
        mIndicatorSpace = dp2px(context, spaceDp)

        mSelectedCirclePointColor = ta.getColor(R.styleable.CirclePointIndicator_selectedCirclePointColor, DEFAULT_SELECTED_CIRCLE_POINT_COLOR)
        mUnselectedCirclePointColor = ta.getColor(R.styleable.CirclePointIndicator_unselectedCirclePointColor, DEFAULT_UNSELECTED_CIRCLE_POINT_COLOR)

        ta.recycle()

        mSelectedCirclePointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mSelectedCirclePointPaint.color = mSelectedCirclePointColor

        mUnselectedCirclePointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mUnselectedCirclePointPaint.color = mUnselectedCirclePointColor

    }

    private fun dp2px(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = mIndicatorRadius * 2
        val width = mIndicatorRadius * 2 * mIndicatorCount + (mIndicatorCount - 1) * mIndicatorSpace
        setMeasuredDimension(width.toInt(), height.toInt())
    }


    override fun onDraw(canvas: Canvas) {
        //未与viewPager绑定 不绘制
        if (!isBindToViewPager)
            return

        //先遍历绘制除了当前的以及当前的下一个
        for (i in 0 until mIndicatorCount) {

            if (i == mCurrentSelectedIndex)
                continue
            if (i == mCurrentSelectedIndex + 1)
                continue

            val x = i * mIndicatorSpace + (i * 2 + 1) * mIndicatorRadius
            val y = mIndicatorRadius
            canvas.drawCircle(x, y, mIndicatorRadius, mUnselectedCirclePointPaint)
        }

        //当前选中的圆点的圆心x坐标
        val selectX1 = mCurrentSelectedIndex * mIndicatorSpace + (mCurrentSelectedIndex * 2 + 1) * mIndicatorRadius
        //当前选中的下一个的圆心的x坐标
        val selectX2 = (mCurrentSelectedIndex + 1) * mIndicatorSpace + ((mCurrentSelectedIndex + 1) * 2 + 1) * mIndicatorRadius
        //使用选中的颜色对应的画笔绘制当前选中的点，并且加上了偏移量
        canvas.drawCircle(selectX1 + mViewPagerOffset * (mIndicatorSpace + mIndicatorRadius * 2), mIndicatorRadius, mIndicatorRadius, mSelectedCirclePointPaint)
        //绘制下一个点 同样加上了偏移量
        canvas.drawCircle(selectX2 + (-mViewPagerOffset) * (mIndicatorSpace + mIndicatorRadius * 2), mIndicatorRadius, mIndicatorRadius, mUnselectedCirclePointPaint)


    }

    //和viewpager绑定的时候调用的方法
    fun bindToViewPager(viewPager: ViewPager) {
        isBindToViewPager = true
        viewPager.addOnPageChangeListener(this)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        var p = position

        if (p > mIndicatorCount - 1) {
            p = mIndicatorCount - 1
        }

        mCurrentSelectedIndex = p
        if (mCurrentSelectedIndex == mIndicatorCount - 1) {
            mViewPagerOffset = 0f
        } else {
            mViewPagerOffset = positionOffset
        }

        postInvalidate()

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {
    }


}
