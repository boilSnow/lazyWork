package com.boilsnow.lib.common.entente

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

/**
 * Description:简化事件
 * Remark:
 */

/**
 * Description:简单封装viewPage页面事件
 * Remark:
 */
abstract class SimpleViewPageChangeListener : ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }
}

/**
 * Description:简单封装tabLayout选中事件
 * Remark:
 */
abstract class SimpleTabSelectedListener : TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }
}