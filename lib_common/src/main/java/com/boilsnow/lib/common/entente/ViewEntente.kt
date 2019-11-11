package com.boilsnow.lib.common.entente

import android.os.Bundle
import com.boilsnow.lib.common.ui.BaseFragment

/**
 * Description:视图事件回调定义
 * Remark:
 */
interface ViewEntente {

    //定义跳转动作回调
    interface OnSkipAction {
        //跳转回调
        fun onSkip4Route(
            fragment: BaseFragment, path: String, requestCode: Int, extras: Bundle? = null
        )
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