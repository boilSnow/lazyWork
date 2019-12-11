package com.boilsnow.lib.common.entente

import com.boilsnow.lib.common.ui.BaseFragment

/**
 * Description:视图事件回调定义
 * Remark:
 */
interface ViewEntente {

    //定义视图动作回调
    interface OnFragmentAction

    //定义视图滚动回调
    interface OnFragmentScrollAction : OnFragmentAction {
        //滚动监听
        fun onFragmentScroll(oldY: Int, newY: Int, flag: Boolean = true)

        //滚动停止监听
        fun onFragmentScrollStop(nowY: Int, isUpScroll: Boolean, flag: Boolean = true)
    }

    //定义列表动作回调
    interface OnListAction {
        fun onUpdateListData(fragment: BaseFragment) {}

        fun onLoadListData(fragment: BaseFragment) {}
    }

    //定义点击回调
    interface OnClickAction {
        //确定点击
        fun onAffirm()

        //取消点击
        fun onCancel() {}
    }
}