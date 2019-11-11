package com.boilsnow.lib.common.view.assist

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * Description:滚动页面适配器
 * Remark:
 */
internal class ScrollPageAdapter constructor(private val views: List<View>) : PagerAdapter() {

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
        container.removeView(views[position])

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(views[position])
        return views[position]
    }

    override fun getCount(): Int = views.size

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean = arg0 === arg1
}