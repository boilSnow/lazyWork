package com.boilsnow.lib.common.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.boilsnow.lib.common.R
import com.boilsnow.lib.common.view.assist.ScrollPageAdapter
import java.util.*

/**
 * Description:分页滚动控件
 * Remark:
 */
class ScrollPageView : RelativeLayout {

    companion object {
        private const val SCROLL = 0x9
    }

    private var mContext: Context
    private var mViewPage: ViewPager
    private var mPageNumber = 0
    private var mDotLayout: LinearLayout
    private var mDotLayoutParams: LinearLayout.LayoutParams? = null
    private var mDotImageResource: Int = R.drawable.l00_enabled_dot
    private var mIsAutoScroll = false
    private var mScrollInterval = 1000L * 3
    private var mNowPage = 0

    private val mHandler = Handler(Handler.Callback { msg ->
        when (msg.what) {
            SCROLL -> if (mIsAutoScroll) {
                scrollOnce()
                sendScrollMessage()
            }
        }
        true
    })

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        this.mContext = context
        LayoutInflater.from(mContext).inflate(R.layout.l00_view_scroll_page, this, true)
        this.mViewPage = this.findViewById(R.id.viewPager) as ViewPager
        this.mDotLayout = this.findViewById(R.id.dotLayout) as LinearLayout
    }

    fun setDotDrawable(dotDrawable: Int) {
        this.mDotImageResource = dotDrawable
    }

    fun setDotLayoutParams(
        dotLayoutParams: LinearLayout.LayoutParams? = null,
        groupLayoutParams: LayoutParams? = null
    ) {
        if (groupLayoutParams != null) mDotLayout.layoutParams = groupLayoutParams
        mDotLayoutParams = dotLayoutParams
    }

    fun setDotLayoutShow(isShow: Boolean) {
        mDotLayout.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun startAutoScroll(interval: Long = 1000 * 3) {
        if (mPageNumber <= 1) return
        mIsAutoScroll = true
        mScrollInterval = interval
        sendScrollMessage()
    }

    fun stopAutoScroll() {
        mIsAutoScroll = false
    }

    interface BindItem<in T> {
        fun bindItem(t: T): View
        fun onPageShow(page: Int) {}
    }

    fun <T> bindViewData(data: List<T>?, bindItem: BindItem<T>) {
        if (data == null || data.isEmpty()) return
        mPageNumber = data.size

        val views = ArrayList<View>()
        val params =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

        for (i in 0 until mPageNumber) {
            val view = bindItem.bindItem(data[i])
            view.layoutParams = params
            views.add(view)
        }
        initDot()

        mViewPage.adapter = ScrollPageAdapter(views)
        mViewPage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mNowPage = position
                bindItem.onPageShow(position)
                updateDot(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        updateDot(0)
    }

    private fun initDot() {
        mDotLayout.removeAllViews()
        if (mDotImageResource == -1) return
        val dotParams = if (mDotLayoutParams != null) mDotLayoutParams else {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.leftMargin = 15
            params
        }

        for (i in 0 until mPageNumber) {
            val dotView = ImageView(mContext)
            dotView.layoutParams = dotParams
            dotView.setImageResource(mDotImageResource)

            mDotLayout.addView(dotView)
        }
    }

    private fun updateDot(current: Int) {
        if (current >= mDotLayout.childCount) return

        mNowPage = current

        for (i in 0 until mPageNumber) mDotLayout.getChildAt(i).isEnabled = i == current
    }

    private fun sendScrollMessage() {
        mHandler.sendEmptyMessageDelayed(SCROLL, mScrollInterval)
    }

    private fun scrollOnce() {
        mNowPage++
        if (mNowPage >= mPageNumber) mNowPage = 0
        mViewPage.currentItem = mNowPage
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
    }

}